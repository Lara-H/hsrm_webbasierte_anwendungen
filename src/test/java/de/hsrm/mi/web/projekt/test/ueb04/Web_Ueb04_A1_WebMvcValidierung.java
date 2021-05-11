package de.hsrm.mi.web.projekt.test.ueb04;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.sichtung.SichtungController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb04_A1_WebMvcValidierung {
    private final String TESTNAME = "Joghurta Biffel";
    private final String TESTORT = "In der Ecke 17, 99441 Vollradisroda";
    private final String TESTDATUM = "2022-07-17";
	private final String TESTBESCHREIBUNG = "Riesige siebzehn an Plakatwand 17 observiert";

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
	@DisplayName("POST /sichtung/meine/neu mit Validierung: Eintrag ok")
	void angebot_neu_post_validate_neueintrag_ok() throws Exception {
		mockmvc.perform(
			post("/sichtung/meine/neu")
			.param("name", TESTNAME)
			.param("ort", TESTORT)
			.param("datum", TESTDATUM)
			.param("beschreibung", TESTBESCHREIBUNG)
			.param("sub","ok")
			.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().is3xxRedirection())
		.andExpect(header().string("Location", "/sichtung/meine"));
	}



	@Test
	@DisplayName("POST /sichtung/meine/neu mit Validierung: Datum falsches Format")
	void angebot_neu_post_validate_haltbarbis_vergangenheit() throws Exception {
		final String FALSCHDATUM = "Erster April!";
		mockmvc.perform(
			post("/sichtung/meine/neu")
			.param("name", TESTNAME)
			.param("ort", TESTORT)
			.param("datum", FALSCHDATUM)
			.param("beschreibung", TESTBESCHREIBUNG)
			.param("sub","ok")
			.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().isOk())
		.andExpect(view().name("sichtung/meine/bearbeiten"))
		.andExpect(model().attributeHasFieldErrors("meinesichtungform", "datum"))
		.andExpect(content().string(containsString(TESTNAME)))
		.andExpect(content().string(containsString(TESTORT)))
		.andExpect(content().string(containsString(TESTBESCHREIBUNG)));
	}


	@Test
	@DisplayName("POST /sichtung/meine/neu mit Validierung: Ort nur Leerzeichen")
	void angebot_neu_post_validate_abholort_ohne_hausnr() throws Exception {
		final String FALSCHORT = "   ";
		mockmvc.perform(
			post("/sichtung/meine/neu")
			.param("name", TESTNAME)
			.param("ort", FALSCHORT)
			.param("datum", TESTDATUM)
			.param("beschreibung", TESTBESCHREIBUNG)
			.param("sub","ok")
			.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().isOk())
		.andExpect(view().name("sichtung/meine/bearbeiten"))
		.andExpect(model().attributeHasFieldErrors("meinesichtungform", "ort"))
		.andExpect(content().string(containsString(TESTNAME)))
		.andExpect(content().string(containsString(TESTDATUM)))
		.andExpect(content().string(containsString(TESTBESCHREIBUNG)));
	}

	@Test
	@DisplayName("POST /sichtung/meine/neu mit Validierung: Ort null")
	void angebot_neu_post_validate_abholort_ohne_plz() throws Exception {
		final String FALSCHORT = null;
		mockmvc.perform(
			post("/sichtung/meine/neu")
			.param("name", TESTNAME)
			.param("ort", FALSCHORT)
			.param("datum", TESTDATUM)
			.param("beschreibung", TESTBESCHREIBUNG)
			.param("sub","ok")
			.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().isOk())
		.andExpect(view().name("sichtung/meine/bearbeiten"))
		.andExpect(model().attributeHasFieldErrors("meinesichtungform", "ort"))
		.andExpect(content().string(containsString(TESTNAME)))
		.andExpect(content().string(containsString(TESTDATUM)))
		.andExpect(content().string(containsString(TESTBESCHREIBUNG)));
	}

	@Test
	@DisplayName("POST /sichtung/meine/neu mit Validierung: Ort Leerstring")
	void angebot_neu_post_validate_abholort_ohne_komma() throws Exception {
		final String FALSCHORT = "";
		mockmvc.perform(
			post("/sichtung/meine/neu")
			.param("name", TESTNAME)
			.param("ort", FALSCHORT)
			.param("datum", TESTDATUM)
			.param("beschreibung", TESTBESCHREIBUNG)
			.param("sub","ok")
			.contentType(MediaType.MULTIPART_FORM_DATA)
		).andExpect(status().isOk())
		.andExpect(view().name("sichtung/meine/bearbeiten"))
		.andExpect(model().attributeHasFieldErrors("meinesichtungform", "ort"))
		.andExpect(content().string(containsString(TESTNAME)))
		.andExpect(content().string(containsString(TESTDATUM)))
		.andExpect(content().string(containsString(TESTBESCHREIBUNG)));
	}


}
