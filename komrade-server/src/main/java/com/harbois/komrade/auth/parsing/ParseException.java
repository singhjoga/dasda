package com.harbois.komrade.auth.parsing;

public class ParseException extends Exception{

	private static final long serialVersionUID = 8470280092605046691L;

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}

}
