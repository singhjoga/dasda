package com.harbois.oauth.server.api.v1.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.harbois.oauth.server.api.v1.common.AbstractService;

@Component
public class OAuthClientService extends AbstractService<OAuthClient, String>{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private OAuthClientRepository repo;
	@Autowired
	public OAuthClientService(OAuthClientRepository repo) {
		super(repo,OAuthClient.class, String.class);
		this.repo=repo;
	}

	@Override
	protected void beforeSave(OAuthClient newObj, boolean isAdd) {
		//if add, encode the secret
		if (isAdd) {
			newObj.setSecret(passwordEncoder.encode(newObj.getSecret()));
		}
	}

	public void updatePassword(String id, String newPassword) {
		OAuthClient obj = getById(id);
		obj.setSecret(passwordEncoder.encode(newPassword));
		save(obj);
	}
}
