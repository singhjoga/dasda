package com.harbois.komrade.v1.credentials;

import com.harbois.komrade.v1.credentials.provider.CredentialsEntry;

public class CredentialsResponse {
	private String key;
	private CredentialsEntry credentialsEntry;
	private String error;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public CredentialsEntry getCredentialsEntry() {
		return credentialsEntry;
	}
	public void setCredentialsEntry(CredentialsEntry credentialsEntry) {
		this.credentialsEntry = credentialsEntry;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
