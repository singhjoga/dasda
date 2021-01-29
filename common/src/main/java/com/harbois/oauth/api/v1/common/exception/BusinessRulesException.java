package com.harbois.oauth.api.v1.common.exception;

public class BusinessRulesException extends TechnicalException{

	private static final long serialVersionUID = -2006019059463060143L;

	public BusinessRulesException(String message, String errorCode) {
		super(message, errorCode);
	}

	public BusinessRulesException(String message, Throwable e, String errorCode) {
		super(message, e, errorCode);
	}

	public BusinessRulesException(String message, Throwable e) {
		super(message, e);
	}

	public BusinessRulesException(String message) {
		super(message);
	}

	public BusinessRulesException(Throwable e) {
		super(e);
	}

}
