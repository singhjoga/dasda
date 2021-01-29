package com.harbois.komrade.runner.exception;

public class PipelineException extends Exception{
	private static final long serialVersionUID = 1L;

	public PipelineException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public PipelineException(String message) {
		super(message);
	}

	public PipelineException(Throwable throwable) {
		super(throwable);
	}
}
