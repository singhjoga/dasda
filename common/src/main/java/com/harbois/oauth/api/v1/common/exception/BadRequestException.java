package com.harbois.oauth.api.v1.common.exception;

import java.util.List;

public class BadRequestException extends TechnicalException{
	private static final long serialVersionUID = -871593672052845759L;

	public BadRequestException(String message, String errorCode) {
		super(message, errorCode);
	}
	public BadRequestException(String message, String errorCode, List<String> errors) {
		super(getErrorStr(message, errors),errorCode);
	}
	
	private static String getErrorStr(String message, List<String> errors) {
		StringBuilder sb = new StringBuilder(message);
		int i=0;
		for (String error: errors) {
			i++;
			sb.append(System.lineSeparator()).append(i).append(". ") .append(error);
		}
		
		return sb.toString();
	}
}
