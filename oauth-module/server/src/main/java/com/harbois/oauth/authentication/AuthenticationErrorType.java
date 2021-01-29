package com.harbois.oauth.authentication;

public enum AuthenticationErrorType {
	USER_NOT_FOUND("User not found"),
	INVALID_CREDENTIALS("Invalid credentials"),
	NOT_PERMITTED_TIME("Not permitted to logon at this time"),
	NOT_PERMITTED_MACHINE("Not permitted to logon at this workstation"),
	PASSWORD_EXPIRED("Password expired"),
	ACCOUNT_DISABLED("Account disabled"),
	ACCOUNT_EXPIRED("Account expired"),
	PASSWORD_RESET("User must reset password"),
	ACCOUNT_LOCKED("User account locked"),
	COMMUNICATION_ERROR("Communicatin Error"),
	UNKNOWN("Unknown Error");
	private String message;

	private AuthenticationErrorType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
}
