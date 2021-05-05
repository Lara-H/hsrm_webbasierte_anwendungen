package de.hsrm.mi.web.projekt.test.ueb03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.login.LoginController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb03_A1_Login {
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
	@DisplayName("GET /login liefert HTML-Seite, die username und password abfragt")
	void login_get_UsernamePassword() throws Exception {
		mockmvc.perform(
			get("/login")
			.contentType("text/html")
		).andExpect(status().isOk())
		.andExpect(content().string(containsString("username")))
		.andExpect(content().string(containsString("password")));
	}

	@Test
	@DisplayName("POST /login mit username und falschem password liefert Passwort-Hilfetext")
	void login_post_falsch_UsernamePassword() throws Exception {
		mockmvc.perform(
			post("/login")
			.param("username", "abc")
			.param("password", "abc")
		).andExpect(status().isOk())
		.andExpect(content().string(containsString("abc3")));
	}

	@Test
	@DisplayName("POST /login mit username und korrektem password ergibt Weiterleitung auf /sichtung/meine")
	void login_post_korrekt_UsernamePassword() throws Exception {
		mockmvc.perform(
			post("/login")
			.param("username", "abc")
			.param("password", "abc3")
		).andExpect(status().is3xxRedirection())
		 .andExpect(header().string("Location", "/sichtung/meine"));
	}


}
