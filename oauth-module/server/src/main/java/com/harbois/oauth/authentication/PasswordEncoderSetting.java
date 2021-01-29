package com.harbois.oauth.authentication;

public class PasswordEncoderSetting {
	private PasswordEncoderType type;
	private String paramValue;
	public PasswordEncoderType getType() {
		return type;
	}
	public void setType(PasswordEncoderType type) {
		this.type = type;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
}
