package com.harbois.komrade.v1.credentials;

import java.util.HashSet;
import java.util.Set;

import com.harbois.komrade.v1.credentials.provider.ProviderCredentialsType;

public class CredentialsRequest {
	private String key;
	private Set<ProviderCredentialsType> requiredTypes=new HashSet<>();
	private String envCode;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public Set<ProviderCredentialsType> getRequiredTypes() {
		return requiredTypes;
	}
	public void setRequiredTypes(Set<ProviderCredentialsType> requiredTypes) {
		this.requiredTypes = requiredTypes;
	}
	public String getEnvCode() {
		return envCode;
	}
	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}
	
}
