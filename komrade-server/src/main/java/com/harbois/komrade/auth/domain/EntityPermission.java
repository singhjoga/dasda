package com.harbois.komrade.auth.domain;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EntityPermission {
	private String entityName;
	@JsonIgnore
	private Map<String, EntityAction> actionsMap=new TreeMap<String, EntityAction>();

	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String name) {
		this.entityName = name;
	}

	public Map<String, EntityAction> getActionsMap() {
		return actionsMap;
	}
	public void setActionsMap(Map<String, EntityAction> actions) {
		this.actionsMap = actions;
	}
	public Collection<EntityAction> getActions() {
		return actionsMap.values();
	}
}
