package com.harbois.oauth.server.api.v1.controllers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.harbois.oauth.server.api.v1.common.exception.AccessDeniedException;

@Controller
public class LoginController extends BaseController{
	private final RestTemplate restTemplate;

	public LoginController(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.basicAuthentication("oauth-server", "123456").build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public @ResponseBody ResponseEntity<?> login(User user) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("scope", "write");
		params.add("username", user.getUserName());
		params.add("password", user.getPassword());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> form = new HttpEntity<MultiValueMap<String, String>>(params, headers);
		
		
		String url = getCurrentURI().toString();
		url = StringUtils.replace(url, "/login", "/oauth/token");
		try {
			OAuthToken token = restTemplate.postForObject(url,form, OAuthToken.class);
			return ResponseEntity.ok(token);
		}catch (BadRequest e) {
			throw new AccessDeniedException("Access denied");
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class User {

		private String userName;
		private String password;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class OAuthToken {

		@JsonProperty("access_token")
		private String accessToken;
		@JsonProperty("refresh_token")
		private String refreshToken;
		@JsonProperty("expires_in")
		private Long expiresIn;

		public OAuthToken() {
		}

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public String getRefreshToken() {
			return refreshToken;
		}

		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}
		public Long getExpiresIn() {
			return expiresIn;
		}

		public void setExpiresIn(Long expiresIn) {
			this.expiresIn = expiresIn;
		}
	}
}
