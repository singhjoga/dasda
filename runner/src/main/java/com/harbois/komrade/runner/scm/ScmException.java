package com.harbois.komrade.runner.scm;

public class ScmException extends Exception{

	private static final long serialVersionUID = 1L;

	public ScmException(String message, Throwable exception) {
		super(message, exception);
	}

	public ScmException(String message) {
		super(message);
	}

	public ScmException(Throwable exception) {
		super(exception);
	}	
}
