package com.harbois.oauth.server.authentication;

public enum PasswordEncoderType {
	NoOp(""),
	Bcrypt("Length"),
	Pbkdf2("Secret"),
	SCrypt(""),
	MD4(""),
	SHA256("Secret");
	private String paramName;

	private PasswordEncoderType(String paramName) {
		this.paramName = paramName;
	}

	public String getParamName() {
		return paramName;
	}
	
}
