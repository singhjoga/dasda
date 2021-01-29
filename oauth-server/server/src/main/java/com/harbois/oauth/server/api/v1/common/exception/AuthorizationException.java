package com.harbois.oauth.server.api.v1.common.exception;

public class AuthorizationException extends TechnicalException{

	private static final long serialVersionUID = -4619016806767898336L;
	public AuthorizationException(String message) {
		super(message);
	}

}
