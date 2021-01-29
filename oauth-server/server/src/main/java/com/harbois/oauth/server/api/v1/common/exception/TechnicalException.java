package com.harbois.oauth.server.api.v1.common.exception;

public class TechnicalException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String INTERNAL_ERROR="99999";
	private String errorCode;
	public TechnicalException(String message, Throwable e) {
		this(message, e, INTERNAL_ERROR);
	}
	public TechnicalException(String message, Throwable e, String errorCode) {
		super(message, e);
		this.errorCode=errorCode;
	}

	public TechnicalException(String message) {
		this(message,INTERNAL_ERROR);
	}
	public TechnicalException(String message, String errorCode) {
		super(message);
		this.errorCode=errorCode;
	}

	public TechnicalException(Throwable e) {
		super(e);
		this.errorCode=INTERNAL_ERROR;
	}
	public String getErrorCode() {
		return errorCode;
	}

}
