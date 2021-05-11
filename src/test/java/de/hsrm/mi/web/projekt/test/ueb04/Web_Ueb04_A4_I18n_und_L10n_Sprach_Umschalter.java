package de.hsrm.mi.web.projekt.test.ueb04;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.sichtung.SichtungController;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb04_A4_I18n_und_L10n_Sprach_Umschalter {
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
	@DisplayName("GET /sichtung/meine?sprache=de Sprache setzen per Query-Parameter")
	void sichtung_liste_deutsch_query_param_de() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.param("sprache","de")  // setze Sprache per Query-Parameter ...?sprache=de
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Datum")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Ort")))
		.andExpect(content().string(containsStringIgnoringCase("Beschreibung")));
	}


	@Test
	@DisplayName("GET /sichtung/meine?sprache=en Sprache setzen per Query-Parameter")
	void sichtung_liste_englisch() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.param("sprache","en")  // setze Sprache in per Query-Parameter ...?sprache=en
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Date")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Place")))
		.andExpect(content().string(containsStringIgnoringCase("Description")));
	}


	@Test
	@DisplayName("GET /sichtung/meine?sprache=en hat Vorrang vor Accept-Language Browsereinstellung")
	void sichtung_liste_browser_deutsch_queryparam_englisch() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.locale(Locale.GERMAN)  // simuliert Accept-Header-Setzung aus Browsereinstellung
			.param("sprache","en")  // setze abweichende Sprache in per Query-Parameter ...?sprache=en
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Date")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Place")))
		.andExpect(content().string(containsStringIgnoringCase("Description")));
	}

	@Test
	@DisplayName("GET /sichtung/meine?sprache=de hat Vorrang vor Accept-Language Browsereinstellung")
	void sichtung_liste_browser_englisch_queryparam_deutsch() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.locale(Locale.ENGLISH)  // simuliert Accept-Header-Setzung aus Browsereinstellung
			.param("sprache","de")   // setze abweichende Sprache in per Query-Parameter ...?sprache=en
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Datum")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Ort")))
		.andExpect(content().string(containsStringIgnoringCase("Beschreibung")));
	}


	@Test
	@DisplayName("GET /sichtung/meine?sprache=en Sprache setzen mit Query-Parameter, dann Seite wechseln")
	void sichtung_liste_englisch_dann_neuerfassung() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.param("sprache","en")  // setze Sprache in per Query-Parameter ...?sprache=de
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Date")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Place")))
		.andExpect(content().string(containsStringIgnoringCase("Description")));

		/*
		 * Wechsel von Übersichtslisten- zu Neuerfassungs-Seite; Spracheinstellung von
		 * Übersichtslisten-Seite sollte erhalten bleiben, da (von Spring) in Session mitgeführt
		 */
		mockmvc.perform(
			get("/sichtung/meine/neu")
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Date")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Place")))
		.andExpect(content().string(containsStringIgnoringCase("Description")));
	}

}
