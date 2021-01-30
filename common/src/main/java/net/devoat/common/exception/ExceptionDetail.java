package net.devoat.common.exception;

import java.util.Map;

public class ExceptionDetail {
	private String message;
	private String errorCode;
	private Map<String, String> details;
	
	public ExceptionDetail() {
		super();
	}
	public ExceptionDetail(String message) {
		this(message,null);
	}
	public ExceptionDetail(String message, String errorCode) {
		this.message = message;
		this.errorCode=errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public Map<String, String> getDetails() {
		return details;
	}
	public void setDetails(Map<String, String> details) {
		this.details = details;
	}
	
}
