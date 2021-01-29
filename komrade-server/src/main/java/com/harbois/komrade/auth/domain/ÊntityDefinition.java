package com.harbois.komrade.auth.domain;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ÊntityDefinition {
	private String name;
	//map os action names and set of constraint variable names
	private Map<String, Set<String>> actions=new TreeMap<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public Map<String, Set<String>> getActions() {
		return actions;
	}

	public void setActions(Map<String, Set<String>> actions) {
		this.actions = actions;
	}
}
