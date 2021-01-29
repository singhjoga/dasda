package com.harbois.komrade.runner.processrunner;

public class StringParameter extends CommandParameter<String>{

	public StringParameter(String prefix, String value) {
		super(prefix, value);
	}
	public StringParameter(String value) {
		super("", value);
	}
	@Override
	public String toDisplayString() {
		return toString();
	}
}
