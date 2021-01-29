package com.harbois.oauth.tests.cleints;

import org.springframework.stereotype.Service;

import com.harbois.common.tests.base.IdentifiableClient;
import com.harbois.common.tests.base.RestException;
import com.harbois.oauth.api.v1.clients.OAuthClient;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.controllers.dtos.Password;
@Service
public class ClientsClient extends IdentifiableClient<OAuthClient, String>{
	public static final String URL="/api/v1/clients";
	public ClientsClient() {
		super(URL, OAuthClient.class, String.class);
	}
	
	public OAuthClient create(String clientId, String grantTypes, String roles, String scopes, String secret) {
		OAuthClient obj = new OAuthClient();
		obj.setAccessTokenValidityMs(5000L);
		obj.setClientId(clientId);
		obj.setGrantTypes(grantTypes);
		obj.setRefreshTokenValidityMs(10000L);
		obj.setRoles(roles);
		obj.setScopes(scopes);
		obj.setSecret(secret);
		
		return obj;
	}
	public RestResponse updatePassword(String id, String newPassword) throws RestException{
		String url=getUrl(id)+"/updatepassword";
		Password obj = new Password(newPassword);
		return updateResource(url, obj);
	}

	@Override
	public OAuthClient createTestObject(String clientId) {
		return create(clientId,"password","TEST_ROLE","read, write","test123");
	}

	@Override
	public void beforeUpdate(OAuthClient obj) {
		obj.setSecret(null); //secret is not updateable
		super.beforeUpdate(obj);
	}
	
}
