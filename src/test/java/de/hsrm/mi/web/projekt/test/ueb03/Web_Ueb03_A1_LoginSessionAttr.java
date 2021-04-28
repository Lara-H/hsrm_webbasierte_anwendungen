package de.hsrm.mi.web.projekt.test.ueb03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.projekt.login.LoginController;

// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/request/MockHttpServletRequestBuilder.html

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Web_Ueb03_A1_LoginSessionAttr {
	public static final String LOGGEDINUSERNAMEATTR = "loggedinusername";
	public static final String TESTUSERNAME = "wackeldackel";
	public static final String TESTPASSWORTOK = TESTUSERNAME+TESTUSERNAME.length();
	public static final String TESTPASSWORTFALSCH = TESTUSERNAME+"boing";

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
	@DisplayName("POST /login mit username und falschem password setzt Session-Attribut loggedinusername auf Leerstring")
	void login_post_falsch_leert_loggedinusername() throws Exception {
		MockHttpSession mysession = new MockHttpSession();
		mysession.setAttribute(LOGGEDINUSERNAMEATTR, "blafasel");
		mockmvc.perform(
			post("/login")
			.param("username", TESTUSERNAME)
			.param("password", TESTPASSWORTFALSCH)
			.session(mysession)
		).andExpect(status().isOk())
		 .andExpect(request().sessionAttribute(LOGGEDINUSERNAMEATTR, is("")));
	}

	@Test
	@DisplayName("POST /login mit username und richtigem password setzt Session-Attribut loggedinusername auf Usernamen")
	void login_post_richtig_setzt_loggedinusername() throws Exception {
		MockHttpSession mysession = new MockHttpSession();
		mysession.setAttribute(LOGGEDINUSERNAMEATTR, "blafasel");
		mockmvc.perform(
			post("/login")
			.param("username", TESTUSERNAME)
			.param("password", TESTPASSWORTOK)
			.session(mysession)
		).andExpect(status().is3xxRedirection())
		 .andExpect(request().sessionAttribute(LOGGEDINUSERNAMEATTR, is(TESTUSERNAME)));;
	}


}
