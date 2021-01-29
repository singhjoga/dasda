package com.harbois.oauth.server.tests.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.harbois.oauth.server.api.v1.common.domain.OAuthToken;

@Component
public class AuthClient extends AbstractClient {
	@Autowired
	private MockMvc mvc;
	private OAuthToken oAuthToken;

	public OAuthToken authenticate() throws Exception {
		return authenticate("admin","123456");
	}
	public OAuthToken authenticate(String username, String password) throws Exception {
		List<BasicNameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("scope", "write"));
		params.add(new BasicNameValuePair("grant_type", "password"));
		MockHttpServletRequestBuilder builder = post("/oauth/token");
		builder.header("Authorization", "Basic "+Base64.encodeBase64String("oauth-server:123456".getBytes()));
		
		MvcResult result = mvc.perform(builder
		            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		            .content(EntityUtils.toString(new UrlEncodedFormEntity(params)))) //
				.andExpect(status().isOk()) //
				.andReturn();
		String jsonString = result.getResponse().getContentAsString();

		return asObject(jsonString, OAuthToken.class);
	}

	public String getAccessToken() throws Exception {
		if (oAuthToken == null) {
			oAuthToken = authenticate();
		}

		return oAuthToken.getAccessToken();
	}
	public String getAccessToken(String username, String password) throws Exception {
		OAuthToken	token = authenticate(username,password);
	
		return token.getAccessToken();
	}
	private static class OAuthRequst {
		public String username;
		public String password;
		public String scope;
		@JsonProperty(value = "grant_type")
		private String grantType;

		public OAuthRequst(String username, String password, String scope, String grantType) {
			super();
			this.username = username;
			this.password = password;
			this.scope = scope;
			this.grantType = grantType;
		}
	}
}
