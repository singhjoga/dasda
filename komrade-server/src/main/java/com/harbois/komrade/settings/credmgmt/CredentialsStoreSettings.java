package com.harbois.komrade.settings.credmgmt;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.common.utils.JsonUtil;
import com.harbois.credmgmt.CredentialsProviders;
import com.harbois.komrade.v1.system.external.ExternalSystem;
import com.harbois.komrade.v1.system.external.ExternalSystemService;

@Component
public class CredentialsStoreSettings {
	private static final String SYSTEM_TYPE_CODE="CredProvider";
	
	@Autowired
	private ExternalSystemService extSysService;
	
	private Set<CredentialsProviders> providers = new TreeSet<>();
	private KeePassSettings keepassSettings;
	private PleasantSettings pleasantSettings;
	
	public void refresh() {
		List<ExternalSystem> extSystems = extSysService.findBySystenType(SYSTEM_TYPE_CODE);
		KeePassSettings newKeePassSettings=null;
		PleasantSettings newPleasantSettings=null;
		Set<CredentialsProviders> newProviders = new TreeSet<>();
		newProviders.add(CredentialsProviders.Internal); //internal is always enabled
		for (ExternalSystem sys: extSystems) {
			if (CredentialsProviders.KeePass.name().equals(sys.getProviderCode())) {
				newKeePassSettings = getKeePassSettings(sys.getSettings());
			}else if (CredentialsProviders.Pleasant.name().equals(sys.getProviderCode())) {
				newPleasantSettings = getPleasantSettings(sys.getSettings());
			}
		}
		//TODO: Initialize Providers
		this.providers=newProviders;
		this.keepassSettings=newKeePassSettings;
		this.pleasantSettings=newPleasantSettings;
	}
	
	public Set<CredentialsProviders> getProviders() {
		return providers;
	}

	public void setProviders(Set<CredentialsProviders> providers) {
		this.providers = providers;
	}

	public KeePassSettings getKeepassSettings() {
		return keepassSettings;
	}

	public void setKeepassSettings(KeePassSettings keepassSettings) {
		this.keepassSettings = keepassSettings;
	}

	public PleasantSettings getPleasantSettings() {
		return pleasantSettings;
	}

	public void setPleasantSettings(PleasantSettings pleasantSettings) {
		this.pleasantSettings = pleasantSettings;
	}
	
	private KeePassSettings getKeePassSettings(String settingsJson) {
		if (StringUtils.isEmpty(settingsJson)) {
			throw new IllegalStateException("KeePass Credentials Provider cannot be activated. Settings are not defined.");
		}
		
		return JsonUtil.getObject(settingsJson, KeePassSettings.class);
	}
	private PleasantSettings getPleasantSettings(String settingsJson) {
		if (StringUtils.isEmpty(settingsJson)) {
			throw new IllegalStateException("Pleasant Credentials Provider cannot be activated. Settings are not defined.");
		}
		
		return JsonUtil.getObject(settingsJson, PleasantSettings.class);
	}
	
}
