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
class Web_Ueb04_A4_I18n_und_L10n {
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
	@DisplayName("GET /sichtung/meine mit Locale.GERMAN")
	void sichtung_liste_deutsch_locale_gesetzt() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.locale(Locale.GERMAN) // entspricht: Browser-Voreinstellung setzt "Accept-Language"-Requestheader
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Datum")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Ort")))
		.andExpect(content().string(containsStringIgnoringCase("Beschreibung")));
	}

	@Test
	@DisplayName("GET /sichtung/meine mit Locale.ENGLISH")
	void sichtung_liste_englisch_locale_gesetzt() throws Exception {
		mockmvc.perform(
			get("/sichtung/meine")
			.locale(Locale.ENGLISH)  // entspricht: Browser-Voreinstellung setzt "Accept-Language"-Requestheader
		).andExpect(status().isOk())
		.andExpect(content().string(containsStringIgnoringCase("Date")))
		.andExpect(content().string(containsStringIgnoringCase("Name")))
		.andExpect(content().string(containsStringIgnoringCase("Place")))
		.andExpect(content().string(containsStringIgnoringCase("Description")));
	}
}
