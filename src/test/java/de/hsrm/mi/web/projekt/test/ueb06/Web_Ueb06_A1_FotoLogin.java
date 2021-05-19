package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.login.LoginController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class Web_Ueb06_A1_FotoLogin {
	public static final String LOGGEDINUSERNAMEATTR = "loggedinusername";
	public static final String TESTUSERNAME = "woihinkelche";
	public static final String TESTPASSWORTOK = TESTUSERNAME+TESTUSERNAME.length();
	public static final String TESTPASSWORTFALSCH = TESTUSERNAME+"123";

	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private LoginController logincontroller;

	@Test
	void vorabcheck() {
		assertThat(logincontroller).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	@Test
	@DisplayName("GET /fotologin liefert HTML-Seite, die username und password abfragt")
	void login_get_UsernamePassword() throws Exception {
		mockmvc.perform(
			get("/fotologin")
			.contentType("text/html")
		).andExpect(status().isOk())
		.andExpect(content().string(containsString("username")))
		.andExpect(content().string(containsString("password")));
	}

	@Test
	@DisplayName("POST /fotologin mit username und falschem password liefert Passwort-Hilfetext")
	void login_post_falsch_UsernamePassword() throws Exception {
		mockmvc.perform(
			post("/fotologin")
			.param("username", "abc")
			.param("password", "abc")
		).andExpect(status().isOk())
		.andExpect(content().string(containsString("abc3")));
	}

	@Test
	@DisplayName("POST /fotologin mit username und korrektem password ergibt Weiterleitung auf /foto")
	void login_post_korrekt_UsernamePassword() throws Exception {
		mockmvc.perform(
			post("/fotologin")
			.param("username", "abc")
			.param("password", "abc3")
		).andExpect(status().is3xxRedirection())
		 .andExpect(header().string("Location", "/foto"));
	}

	@Test
	@DisplayName("POST /fotologin mit username und falschem password setzt Session-Attribut loggedinusername auf Leerstring")
	void login_post_falsch_leert_loggedinusername() throws Exception {
		MockHttpSession mysession = new MockHttpSession();
		mysession.setAttribute(LOGGEDINUSERNAMEATTR, "blafasel");
		mockmvc.perform(
			post("/fotologin")
			.param("username", TESTUSERNAME)
			.param("password", TESTPASSWORTFALSCH)
			.session(mysession)
		).andExpect(status().isOk())
		 .andExpect(request().sessionAttribute(LOGGEDINUSERNAMEATTR, is("")));
	}

	@Test
	@DisplayName("POST /fotologin mit username und richtigem password setzt Session-Attribut loggedinusername auf Usernamen")
	void login_post_richtig_setzt_loggedinusername() throws Exception {
		MockHttpSession mysession = new MockHttpSession();
		mysession.setAttribute(LOGGEDINUSERNAMEATTR, "blafasel");
		mockmvc.perform(
			post("/fotologin")
			.param("username", TESTUSERNAME)
			.param("password", TESTPASSWORTOK)
			.session(mysession)
		).andExpect(status().is3xxRedirection())
		 .andExpect(request().sessionAttribute(LOGGEDINUSERNAMEATTR, is(TESTUSERNAME)));;
	}

}
