package com.harbois.oauth.server.api.v1.common.exception;

public class AccessDeniedException extends TechnicalException{

	private static final long serialVersionUID = -12069870686023303L;

	public AccessDeniedException(String msg) {
		super(msg);
	}

}
