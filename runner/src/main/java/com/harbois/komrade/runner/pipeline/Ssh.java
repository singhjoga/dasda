package com.harbois.komrade.runner.pipeline;

public class Ssh {
	private String host;
	private String credentialsId;
	private String username;
	private String password;
	private String sshKey;
	private Boolean doSudo=false;
	private String switchUsername;
	private String switchPassword;
	private Boolean doSwitchUserSudo=false;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
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
	public Boolean getDoSudo() {
		return doSudo;
	}
	public Boolean getDoSwitchUserSudo() {
		return doSwitchUserSudo;
	}
	public void setDoSwitchUserSudo(Boolean doSwitchUserSudo) {
		this.doSwitchUserSudo = doSwitchUserSudo;
	}
	public void setDoSudo(Boolean doSudo) {
		this.doSudo = doSudo;
	}
	public String getSwitchUsername() {
		return switchUsername;
	}
	public void setSwitchUsername(String switchUsername) {
		this.switchUsername = switchUsername;
	}
	public String getSwitchPassword() {
		return switchPassword;
	}
	public void setSwitchPassword(String switchPassword) {
		this.switchPassword = switchPassword;
	}	
}
