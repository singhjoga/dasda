package com.harbois.oauth.server.authentication.internaldb;

import com.harbois.oauth.server.authentication.AuthSettings;

public class InternalDbAuthSettings extends AuthSettings {
	private AuthorityType returnAuthorityType;

	public AuthorityType getReturnAuthorityType() {
		return returnAuthorityType;
	}

	public void setReturnAuthorityType(AuthorityType returnAuthorityType) {
		this.returnAuthorityType = returnAuthorityType;
	}

	public static enum AuthorityType {
		Groups, Roles, Permissions;
		public boolean isGroups() {
			return this==Groups;
		}
		public boolean isRoles() {
			return this==Roles;
		}
		public boolean isPermissions() {
			return this==Permissions;
		}
	}
}
