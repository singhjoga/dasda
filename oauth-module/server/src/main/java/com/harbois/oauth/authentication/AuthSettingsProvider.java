package com.harbois.oauth.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harbois.oauth.api.v1.clients.OAuthClient;
import com.harbois.oauth.api.v1.clients.OAuthClientService;
import com.harbois.oauth.api.v1.common.UserContext;
import com.harbois.oauth.api.v1.groups.GroupService;
import com.harbois.oauth.api.v1.roles.RoleService;
import com.harbois.oauth.api.v1.settings.AppAuthSetting;
import com.harbois.oauth.api.v1.settings.AppAuthSettingService;
import com.harbois.oauth.api.v1.settings.AuthProviderType;
import com.harbois.oauth.api.v1.users.UserService;
import com.harbois.oauth.authentication.ad.AdAuthProvider;
import com.harbois.oauth.authentication.ad.AdAuthSettings;
import com.harbois.oauth.authentication.internaldb.InternalDbAuthProvider;
import com.harbois.oauth.authentication.internaldb.InternalDbAuthSettings;
import com.harbois.oauth.authentication.jdbc.JdbcAuthProvider;
import com.harbois.oauth.authentication.jdbc.JdbcAuthSettings;
import com.harbois.oauth.authentication.ldap.LdapAuthProvider;
import com.harbois.oauth.authentication.ldap.LdapAuthSettings;

@Component
public class AuthSettingsProvider {
	private static Logger LOG = LoggerFactory.getLogger(AuthSettingsProvider.class);
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Autowired
	private UserService userService;
	@Autowired
	private OAuthClientService clientService;	
	@Autowired
	private RoleService roleService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	@Autowired
	private AppAuthSettingService service;

	private Map<String,List<CustomAuthenticationProvider>> providers;

	@PostConstruct
	public void init() {
		providers = new HashMap<>();
		
		for (OAuthClient client: clientService.findAllAsList()) {
			loadClientSettings(client.getClientId());
		}
		LOG.info("Auth providers loaded");
	}
	private void loadClientSettings(String clientId) {
		Iterable<AppAuthSetting> settings = service.findAllByClientId(clientId);
		List<CustomAuthenticationProvider> result = new ArrayList<CustomAuthenticationProvider>();
		settings.forEach(s -> result.add(buildProvider(s)));
		providers.put(clientId, result);
	}
	public List<CustomAuthenticationProvider> getProviders(String clientId) {
		List<CustomAuthenticationProvider> result = providers.get(clientId);
		if (result==null) {
			//should never be the case;
			LOG.error("Providers not initialized for client: "+clientId);
			return Collections.emptyList();
		}
		return result;
	}

	private CustomAuthenticationProvider buildProvider(AppAuthSetting setting) {
		try {
			if (setting.getProviderType() == AuthProviderType.ActiveDirectory) {
				AdAuthSettings authSettings;
				authSettings = jsonMapper.readValue(setting.getSettingsJson(), AdAuthSettings.class);
				return new AdAuthProvider(authSettings);
			} else if (setting.getProviderType() == AuthProviderType.InternalDatabase) {
				InternalDbAuthSettings authSettings = jsonMapper.readValue(setting.getSettingsJson(), InternalDbAuthSettings.class);
				return new InternalDbAuthProvider(authSettings,userService,roleService,groupService,passwordEncoder);
			} else if (setting.getProviderType() == AuthProviderType.ExternalDatabase) {
				JdbcAuthSettings authSettings = jsonMapper.readValue(setting.getSettingsJson(), JdbcAuthSettings.class);
				return new JdbcAuthProvider(authSettings);
			} else if (setting.getProviderType() == AuthProviderType.LDAP) {
				LdapAuthSettings authSettings = jsonMapper.readValue(setting.getSettingsJson(), LdapAuthSettings.class);
				return new LdapAuthProvider(authSettings);
			}else {
				throw new IllegalStateException("Auth provider not supported: "+setting.getProviderType().name());
			}
				
		} catch (IOException e) {
			throw new IllegalStateException("Error parsing settings json for " + setting.getName());
		}
	}
}
