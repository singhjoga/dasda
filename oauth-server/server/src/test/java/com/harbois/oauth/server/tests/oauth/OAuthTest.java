package com.harbois.oauth.server.tests.oauth;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.harbois.oauth.server.api.v1.common.domain.OAuthToken;
import com.harbois.oauth.server.tests.base.AuthClient;
import com.harbois.oauth.server.tests.base.TestBase;

public class OAuthTest extends TestBase {

	@Autowired
	private AuthClient auth;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	public void testOAuth() throws Exception {
		OAuthToken token = auth.authenticate();

	}
	@Test
	public void genPass() throws Exception {
		System.out.println(passwordEncoder.encode("default-user123"));
	}
}
