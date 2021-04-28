package de.hsrm.mi.web.projekt.test.ueb03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.sichtung.Sichtung;
import de.hsrm.mi.web.projekt.sichtung.SichtungController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb03_A4_SichtungLoeschen {
    private final String TESTNAME = "Joendhard";
    private final String TESTORT = "In der Ecke 17, 99441 Vollradisroda";
	private final LocalDate TESTLOCALDATE = LocalDate.now();
	private final String TESTBESCHREIBUNG = "Ich habe 17 Koffer voll Kartoffelpuffer.";

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
	@DisplayName("GET /sichtung/meine/0/del sollte 0. Eintrag in Sichtungen-Liste löschen")
	void angebot_neu_post() throws Exception {
		// Sessionattribut vorbereiten
		Map<String, Object> sessiondata = new HashMap<>();
		List<Sichtung> liste = new ArrayList<>();
		Sichtung s = new Sichtung();
		s.setName(TESTNAME);
		s.setDatum(TESTLOCALDATE);
		s.setOrt(TESTORT);
		s.setBeschreibung(TESTBESCHREIBUNG);
		liste.add(s);
		sessiondata.put("meinesichtungen", liste);

		// und gucken, ob Testeintrag in der /sichtung/meine-Liste auftaucht
		mockmvc.perform(
			get("/sichtung/meine")
			.sessionAttrs(sessiondata)
		)
		.andExpect(content().string(containsString(TESTNAME)))
		.andExpect(content().string(containsString(TESTORT)))
		.andExpect(content().string(containsString(TESTBESCHREIBUNG)));

		// Ein Eintrag sollte in der Liste sein
		assertEquals(1, ((List<?>) sessiondata.get("meinesichtungen")).size());

		// und mit del-Link löschen, sollte auf Listen-Seite umleiten
		mockmvc.perform(
			get("/sichtung/meine/0/del")
			.sessionAttrs(sessiondata)
		).andExpect(status().is3xxRedirection())
		.andExpect(header().string("Location", "/sichtung/meine"));

		// Liste in Sessionattribut "meinesichtungen" sollte nun leer sein
		assertEquals(0, ((List<?>) sessiondata.get("meinesichtungen")).size());

	}


}
