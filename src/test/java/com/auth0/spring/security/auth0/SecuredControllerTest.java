package com.auth0.spring.security.auth0;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SecuredControllerTest extends MVCBaseSecurityTest {

	@Test
	public void shouldReturn403WithoutToken() throws Exception {
		callUrlWithoutToken("/secured").andExpect(status().isForbidden());
	}

	@Test
	public void shouldReturn401ForAnInvalidToken() throws Exception {
		callUrlWithToken("/secured", "a.b.c").andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldReturn401ForATokenThatHasExpired() throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);
		String token = generateTokenWithExpirationDate(c.getTimeInMillis() / 1000L);
		callUrlWithToken("/secured", token).andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldReturn200ForAValidToken() throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);
		callUrlWithToken("/secured", generateTokenWithExpirationDate(c.getTimeInMillis() / 1000L)).andExpect(status().isOk());
	}

	@Test
	public void shouldReturn200ForAnUnsecuredUrl() throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);

		callUrlWithToken("/unsecured", generateTokenWithExpirationDate(c.getTimeInMillis() / 1000L)).andExpect(status().isOk());
	}

}
