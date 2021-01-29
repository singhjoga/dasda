package com.harbois.komrade.v1.credentials.provider;

import com.harbois.oauth.api.v1.common.exception.BusinessException;

public class CredentialsException extends BusinessException {
	private static final long serialVersionUID = -6500914544589044762L;

	public CredentialsException(String message, String errorCode) {
		super(message, errorCode);
	}

	public CredentialsException(String message, Throwable e, String errorCode) {
		super(message, e, errorCode);
	}

	public CredentialsException(String message, Throwable e) {
		super(message, e);
	}

	public CredentialsException(String message) {
		super(message);
	}

	public CredentialsException(Throwable e) {
		super(e);
	}

}
