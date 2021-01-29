package com.harbois.komrade.v1.credentials.provider;

public class CredentialsEntry {
	private String id;
	private String username;
	private String password;
	private String sshKey;
	private String title;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
