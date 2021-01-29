package com.harbois.komrade.settings;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.settings.credmgmt.CredentialsStoreSettings;

@Component
public class SystemSettings {
	private static Logger LOG = LoggerFactory.getLogger(SystemSettings.class);
	@Autowired
	private CredentialsStoreSettings credentialsStoreSettings;
	
	@PostConstruct
	public void init() {
		LOG.info("Initializing system...");
		credentialsStoreSettings.refresh();
		LOG.info("System initialized");
	}
}
