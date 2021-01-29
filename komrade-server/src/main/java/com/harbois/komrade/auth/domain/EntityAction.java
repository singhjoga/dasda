package com.harbois.komrade.auth.domain;

import java.util.Collection;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thetechnovator.common.java.StringProperties;

public class EntityAction {
	private String name;
	@JsonIgnore
	private StringProperties constraintsObj=new StringProperties();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public StringProperties getConstraintObj() {
		return constraintsObj;
	}
	public void setConstraintsObj(StringProperties constraints) {
		this.constraintsObj = constraints;
	}
	public Collection<Entry<String, String>> getConstraints() {
		return constraintsObj.entrySet();
	}
}
