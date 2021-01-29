package com.harbois.komrade.runner.pipeline;

public class Credentials {
	private String credentialsId;
	private String username;
	private String password;
	private String sshKey;
	public String getCredentialsId() {
		return credentialsId;
	}
	public void setCredentialsId(String credentialsId) {
		this.credentialsId = credentialsId;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSshKey() {
		return sshKey;
	}
	public void setSshKey(String sshKey) {
		this.sshKey = sshKey;
	}
	
}
