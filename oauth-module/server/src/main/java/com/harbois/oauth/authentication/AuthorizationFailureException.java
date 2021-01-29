package com.harbois.oauth.authentication;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationFailureException extends AuthenticationException{
	private static final long serialVersionUID = -1305514853752642414L;
	private AuthenticationErrorType errorType;
	public AuthorizationFailureException(String msg) {
		this(msg,null);
	}
	public AuthorizationFailureException(String msg, AuthenticationErrorType errorType) {
		this(msg,errorType,null);
	}
	public AuthorizationFailureException(String msg,AuthenticationErrorType errorType,Throwable t) {
		super(msg, t);
		this.errorType=errorType;
	}
	public AuthenticationErrorType getErrorType() {
		return errorType;
	}

}
