package com.harbois.oauth.server.authentication;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailureException extends AuthenticationException{
	private static final long serialVersionUID = -1305514853752642414L;
	private AuthenticationErrorType errorType;
	public AuthenticationFailureException(String msg) {
		this(msg,null);
	}
	public AuthenticationFailureException(String msg, AuthenticationErrorType errorType) {
		this(msg,errorType,null);
	}
	public AuthenticationFailureException(String msg,AuthenticationErrorType errorType,Throwable t) {
		super(msg, t);
		this.errorType=errorType;
	}
	public AuthenticationErrorType getErrorType() {
		return errorType;
	}

}
