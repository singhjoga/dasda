package com.harbois.komrade.runner.exception;

public class StepFailureException extends PipelineException{
	private static final long serialVersionUID = -4276521726628391558L;

	public StepFailureException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public StepFailureException(String message) {
		super(message);
	}

	public StepFailureException(Throwable throwable) {
		super(throwable);
	}

}
