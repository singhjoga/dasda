package com.harbois.komrade.runner.processrunner;

public  abstract class CommandParameter<T> {
	protected String prefix;
	protected T value;
	public CommandParameter(String prefix, T value) {
		super();
		this.prefix = prefix;
		this.value = value;
	}
	public String getPrefix() {
		return prefix;
	}
	public T getValue() {
		return value;
	}
	@Override
	public String toString() {
		return (prefix==null?"":prefix)+value.toString();
	}
	public abstract String toDisplayString();
}
