package com.harbois.komrade.v1.credentials.provider;

public enum CredentialsType {
	Username("UID"),//
	Password("PWD"),//
	SshKey("SSK");
	
	private String code;

	private CredentialsType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	public static CredentialsType forCode(String typeCode) {
		for (CredentialsType type: CredentialsType.values()) {
			if (type.code.equals(typeCode)) {
				return type;
			}
		}
		
		return null;
	}
}
