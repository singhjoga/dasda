package com.thetechnovator.common.java.exceptions;

public class BusinessException extends Exception {
	private static final long serialVersionUID = 1L;
	private static final String INTERNAL_ERROR="99999";
	private String errorCode;
	public BusinessException(String message, Throwable e) {
		this(message, e, INTERNAL_ERROR);
	}
	public BusinessException(String message, Throwable e, String errorCode) {
		super(message, e);
		this.errorCode=errorCode;
	}

	public BusinessException(String message) {
		this(message,INTERNAL_ERROR);
	}
	public BusinessException(String message, String errorCode) {
		super(message);
		this.errorCode=errorCode;
	}

	public BusinessException(Throwable e) {
		super(e);
		this.errorCode=INTERNAL_ERROR;
	}
	public String getErrorCode() {
		return errorCode;
	}
}
