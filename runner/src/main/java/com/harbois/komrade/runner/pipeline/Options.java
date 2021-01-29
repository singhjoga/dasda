package com.harbois.komrade.runner.pipeline;

import com.thetechnovator.common.java.StringProperties;

public class Options {
	private ScmRepository scmRepository;
	//private List<KeyValue<String>>variables;
	private StringProperties variables;
	

	public StringProperties getVariables() {
		return variables;
	}

	public void setVariables(StringProperties variables) {
		this.variables = variables;
	}

	public ScmRepository getScmRepository() {
		return scmRepository;
	}

	public void setScmRepository(ScmRepository scmRepository) {
		this.scmRepository = scmRepository;
	}
	
}
