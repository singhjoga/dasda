package com.harbois.oauth.server.api.v1.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.oauth.server.api.v1.common.AbstractService;

@Component
public class AppAuthSettingService extends AbstractService<AppAuthSetting, Long>{
	private AppAuthSettingRepository repo;
	@Autowired
	public AppAuthSettingService(AppAuthSettingRepository repo) {
		super(repo,AppAuthSetting.class, Long.class);
		this.repo=repo;
	}
	public List<AppAuthSetting> findAllByClientId(String clientId) {
		return repo.findAllByClientId(clientId);
	}
}
