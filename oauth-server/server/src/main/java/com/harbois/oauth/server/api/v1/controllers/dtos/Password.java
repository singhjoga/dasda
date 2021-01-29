package com.harbois.oauth.server.api.v1.controllers.dtos;

public class Password {
	private String password;

	public Password(String password) {
		super();
		this.password = password;
	}

	public Password() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
}
