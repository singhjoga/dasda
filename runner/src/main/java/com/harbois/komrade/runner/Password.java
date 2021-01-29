package com.harbois.komrade.runner;

public class Password{
	private char[] value;
	public Password(char[] value) {
		super();
		this.value = value;
	}
	public Password(String value) {
		super();
		this.value = value.toCharArray();
	}
	public char[] getValue() {
		return value;
	}
	@Override
	public String toString() {
		return new String(value);
	}

}
