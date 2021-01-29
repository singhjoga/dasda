package com.harbois.komrade.runner.processrunner;

import com.harbois.komrade.runner.Password;

public class PasswordParameter extends CommandParameter<Password>{

	public PasswordParameter(String prefix, Password value) {
		super(prefix, value);
	}
	public PasswordParameter(Password value) {
		super("", value);
	}
	@Override
	public String toDisplayString() {
		return (prefix==null?"":prefix)+"******";
	}
	
}
