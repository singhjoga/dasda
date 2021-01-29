package com.harbois.oauth.server.tests.base;

import com.harbois.oauth.server.api.v1.common.exception.ExceptionDetail;

public class RestException extends Exception{
	private static final long serialVersionUID = -7667846882268906956L;

	private ExceptionDetail exceotionDetail;
	
	public RestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RestException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public RestException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public RestException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public RestException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ExceptionDetail getExceotionDetail() {
		return exceotionDetail;
	}

	public void setExceotionDetail(ExceptionDetail exceotionDetail) {
		this.exceotionDetail = exceotionDetail;
	}

}
