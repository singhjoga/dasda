package com.thetechnovator.common.java.exceptions;

public class RestException extends RuntimeException{
	private static final long serialVersionUID = -7044181353234996629L;
	private String errorCode;
	private int httpStatus;
	public RestException(String message, Throwable t, String errorCode, int httpStatus) {
		super(message, t);
		this.errorCode=errorCode;
		this.httpStatus=httpStatus;
	}
	public RestException(String message, String errorCode, int httpStatus) {
		this(message,null,errorCode,httpStatus);
	}
	public RestException(String message) {
		this(message,null,null,500);
	}
	public RestException(Throwable t) {
		this(t.getMessage(),t,null,500);
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
}
