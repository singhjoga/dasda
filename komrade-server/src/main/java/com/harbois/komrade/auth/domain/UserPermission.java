package com.harbois.komrade.auth.domain;

public class UserPermission {
	private String username;
	//entity and allowed actions
	private EntityPermissions entityPermissions=new EntityPermissions();
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public EntityPermissions getEntityPermissions() {
		return entityPermissions;
	}
	public void setEntityPermissions(EntityPermissions entityPermissions) {
		this.entityPermissions = entityPermissions;
	}

}
