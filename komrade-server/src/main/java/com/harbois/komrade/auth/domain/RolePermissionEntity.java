package com.harbois.komrade.auth.domain;

public class RolePermissionEntity {
	private EntityPermissions allowedEntities =new EntityPermissions();
//	Set<String> notAllowedEntities=new TreeSet<>();
//	Map<String,Set<String>> notAllowedActions = new TreeMap<>();
	

	public EntityPermissions getAllowedEntities() {
		return allowedEntities;
	}

	public void setAllowedEntities(EntityPermissions allowedEntities) {
		this.allowedEntities = allowedEntities;
	}
/*
	public Set<String> getNotAllowedEntities() {
		return notAllowedEntities;
	}

	public void setNotAllowedEntities(Set<String> notAllowedEntities) {
		this.notAllowedEntities = notAllowedEntities;
	}

	public Map<String, Set<String>> getNotAllowedActions() {
		return notAllowedActions;
	}

	public void setNotAllowedActions(Map<String, Set<String>> notAllowedActions) {
		this.notAllowedActions = notAllowedActions;
	}
*/

}
