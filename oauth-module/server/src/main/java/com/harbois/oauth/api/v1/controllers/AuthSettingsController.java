package com.harbois.oauth.api.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.oauth.api.v1.settings.AppAuthSetting;
import com.harbois.oauth.api.v1.settings.AppAuthSettingService;

@RestController
@RequestMapping("/api/v1/authsettings")
public class AuthSettingsController extends BaseResourceController<AppAuthSetting, Long> {
	private AppAuthSettingService service;
	@Autowired
	public AuthSettingsController(AppAuthSettingService service) {
		super(service);
		this.service=service;
	}
}
