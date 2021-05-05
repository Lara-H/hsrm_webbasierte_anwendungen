package de.hsrm.mi.web.projekt.test.ueb03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.sichtung.Sichtung;
import de.hsrm.mi.web.projekt.sichtung.SichtungController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb03_A4_SichtungAnlegen {
    private final String TESTNAME = "Joghurta";
    private final String TESTORT = "In der Ecke 17, 99441 Vollradisroda";
    private final String TESTDATUM = "2022-07-17";
	private final String TESTBESCHREIBUNG = "17 Waschbären schlichen um mein Auto";

	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private SichtungController sichtungctrlr;

	@Test
	void vorabcheck() {
		assertThat(sichtungctrlr).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	@Test
	@DisplayName("GET /sichtung/meine liefert HTML")
	void angebot_get() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.contentType("text/html")
		).andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET /sichtung/meine liefert HTML und legt 'meinesichtungen' Sessionattribut an")
	void angebot_get_sessionattr() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.contentType("text/html")
		).andExpect(status().isOk())
		.andExpect(request().sessionAttribute("meinesichtungen", isA(List.class)));
	}

	@Test
	@DisplayName("GET /sichtung/meine/neu liefert HTML-Seite")
	void angebot_neu_get() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine/neu")
			.contentType("text/html")
		).andExpect(status().isOk());

	}
	
	@Test
	@DisplayName("POST /sichtung/meine/neu mit Formulardaten für Sichtung-Attribute führt zu /sichtung/meine")
	void angebot_neu_post() throws Exception {
		// Sessionattribut vorbereiten
		Map<String, Object> sessiondata = new HashMap<>();
		sessiondata.put("meinesichtungen", new ArrayList<Sichtung>());

		// Eintrag anlegen, sollte abschließend auf Listen-Seite /sichtung/meine umleiten
		mockmvc.perform(
			post("/sichtung/meine/neu")
			.param("name", TESTNAME)
			.param("ort", TESTORT)
			.param("datum", TESTDATUM)
			.param("beschreibung", TESTBESCHREIBUNG)
			.param("sub","ok")
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.sessionAttrs(sessiondata)
		).andExpect(status().is3xxRedirection())
		 .andExpect(header().string("Location", "/sichtung/meine"));
		
		// und gucken, ob er in der /sichtung/meine-Liste auftaucht
		mockmvc.perform(
			get("/sichtung/meine")
			.sessionAttrs(sessiondata)
		)
		.andExpect(content().string(containsString(TESTNAME)))
		.andExpect(content().string(containsString(TESTORT)))
		.andExpect(content().string(containsString(TESTBESCHREIBUNG)));
	}

	@Test
	@DisplayName("Mehrere POST auf /sichtung/meine/neu zum Anlegen mehrerer Listeneinträge auf /angebot")
	void angebot_neu_post_many() throws Exception {
		String[][] w = {
			{"Albert", "Astweg 1, 52070 Aachen", "2022-01-11", "Habe 17 Erbsen gezählt"},
			{"Berta", "Bommel Berg 25, 10115 Berlin", "2022-02-22", "Ganze 17 Euro gewonnen"},
			{"Charles", "Chemieplatz 456, 09117 Chemnitz", "2022-03-03", "Doppelsiebzehnten Geburtstag gefeiert"},
			{"Dora-Viktualia ", "In den Blamuesen 1234, 40489 Duesseldorf bei Krefeld", "2022-04-04", "Meine 17 ist kaputt."},
		};

		Map<String, Object> sessiondata = new HashMap<>();
		sessiondata.put("meinesichtungen", new ArrayList<Sichtung>());

		// Session aufmachen
		mockmvc.perform(
			get("/sichtung/meine")
			.sessionAttrs(sessiondata)
		).andExpect(status().isOk());

		int nAnfang = ((List<?>)sessiondata.get("meinesichtungen")).size();

		// Einträge anlegen, sollte jeweils einen Redirect auf Listen-Seite /sichtung/meine ergeben
		for (String[] a: w) {
			mockmvc.perform(
				post("/sichtung/meine/neu")
				.sessionAttrs(sessiondata)
				.param("name", a[0])
				.param("ort", a[1])
				.param("datum", a[2])
				.param("beschreibung", a[3])
				.param("sub","ok")
				.contentType(MediaType.MULTIPART_FORM_DATA)
			).andExpect(status().is3xxRedirection())
			 .andExpect(header().string("Location", "/sichtung/meine"));
		}

		// es sind genau |w|-viele Einträge hinzugekommen.
		List<?> lst = (List<?>) sessiondata.get("meinesichtungen");
		assertEquals(nAnfang + w.length, lst.size());

		// mal Liste abrufen und gucken, ob wir alles wiederfinden
		var res = mockmvc.perform(
			get("/sichtung/meine")
			.sessionAttrs(sessiondata)
		);

		for (String[] a: w) {
			res
			.andExpect(content().string(containsString(a[0])))
			.andExpect(content().string(containsString(a[1])))
			.andExpect(content().string(containsString(a[3])))
			// immer (ab 1. Runde) müssen alle drin sein, also zumindest #1
			.andExpect(content().string(containsString(w[0][0])))
			.andExpect(content().string(containsString(w[0][1])))
			.andExpect(content().string(containsString(w[0][3])))
			;
		}
	}
}
