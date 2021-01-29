package com.harbois.komrade.runner.exception;

public class CommandExectionException extends Exception {
	private static final long serialVersionUID = 9074919936784151477L;

	public CommandExectionException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CommandExectionException(String message) {
		super(message);
	}

	public CommandExectionException(Throwable throwable) {
		super(throwable);
	}

}
