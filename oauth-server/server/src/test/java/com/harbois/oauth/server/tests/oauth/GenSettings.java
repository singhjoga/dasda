package com.harbois.oauth.server.tests.oauth;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harbois.oauth.server.authentication.AuthSettings.WhenRolesFound;
import com.harbois.oauth.server.authentication.AuthSettings.WhenRolesNotFound;
import com.harbois.oauth.server.authentication.AuthSettings.WhenUserNotFound;
import com.harbois.oauth.server.authentication.ad.AdAuthSettings;
import com.harbois.oauth.server.authentication.internaldb.InternalDbAuthSettings;
import com.harbois.oauth.server.authentication.internaldb.InternalDbAuthSettings.AuthorityType;
import com.harbois.oauth.server.authentication.jdbc.JdbcAuthSettings;
import com.harbois.oauth.server.authentication.ldap.LdapAuthSettings;

public class GenSettings {
	private ObjectMapper mapper = new ObjectMapper();
	@Test
	public void getInternalDbJson() throws JsonProcessingException {
		InternalDbAuthSettings settings = new InternalDbAuthSettings();
		settings.setCanAuthenticate(true);
		settings.setCanAuthorize(true);
		settings.setReturnAuthorityType(AuthorityType.Roles);
		settings.setWhenRolesFound(WhenRolesFound.Continue);
		settings.setWhenRolesNotFound(WhenRolesNotFound.Continue);
		settings.setWhenUserNotFound(WhenUserNotFound.Continue);
		
		String json = mapper.writeValueAsString(settings);
		System.out.println(json);
	}
	@Test
	public void getJdbcJson() throws JsonProcessingException {
		JdbcAuthSettings settings = new JdbcAuthSettings();
		settings.setCanAuthenticate(true);
		settings.setCanAuthorize(true);
		settings.setWhenRolesFound(WhenRolesFound.Continue);
		settings.setWhenRolesNotFound(WhenRolesNotFound.Continue);
		settings.setWhenUserNotFound(WhenUserNotFound.Continue);

		String json = mapper.writeValueAsString(settings);
		System.out.println(json);
	}
	@Test
	public void getAdJson() throws JsonProcessingException {
		AdAuthSettings settings = new AdAuthSettings();
		settings.setCanAuthenticate(true);
		settings.setCanAuthorize(true);
		settings.setWhenRolesFound(WhenRolesFound.Continue);
		settings.setWhenRolesNotFound(WhenRolesNotFound.Continue);
		settings.setWhenUserNotFound(WhenUserNotFound.Continue);
		
		String json = mapper.writeValueAsString(settings);
		System.out.println(json);
	}
	@Test
	public void getLdapJson() throws JsonProcessingException {
		LdapAuthSettings settings = new LdapAuthSettings();
		settings.setCanAuthenticate(true);
		settings.setCanAuthorize(true);
		settings.setWhenRolesFound(WhenRolesFound.Continue);
		settings.setWhenRolesNotFound(WhenRolesNotFound.Continue);
		settings.setWhenUserNotFound(WhenUserNotFound.Continue);
		
		String json = mapper.writeValueAsString(settings);
		System.out.println(json);
	}
}
