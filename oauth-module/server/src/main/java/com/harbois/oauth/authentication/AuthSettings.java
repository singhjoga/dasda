package com.harbois.oauth.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AuthSettings {
	@JsonProperty(value="canAuthenticate")
	private boolean canAuthenticate=false;
	@JsonProperty(value="canAuthorize")
	private boolean canAuthorize=false;
	private WhenUserNotFound whenUserNotFound;
	private WhenRolesNotFound whenRolesNotFound;
	private WhenRolesFound whenRolesFound;
	
	public boolean canAuthenticate() {
		return canAuthenticate;
	}
	public void setCanAuthenticate(boolean authenticate) {
		this.canAuthenticate = authenticate;
	}
	public boolean canAuthorize() {
		return canAuthorize;
	}
	public void setCanAuthorize(boolean authorize) {
		this.canAuthorize = authorize;
	}

	public WhenUserNotFound getWhenUserNotFound() {
		return whenUserNotFound;
	}
	public WhenRolesNotFound getWhenRolesNotFound() {
		return whenRolesNotFound;
	}
	public WhenRolesFound getWhenRolesFound() {
		return whenRolesFound;
	}


	public void setWhenUserNotFound(WhenUserNotFound whenUserNotFound) {
		this.whenUserNotFound = whenUserNotFound;
	}
	public void setWhenRolesNotFound(WhenRolesNotFound whenRolesNotFound) {
		this.whenRolesNotFound = whenRolesNotFound;
	}
	public void setWhenRolesFound(WhenRolesFound whenRolesFound) {
		this.whenRolesFound = whenRolesFound;
	}


	public static enum WhenUserNotFound {
		Continue,
		ReturnFailure;
		public boolean returnFailure() {
			return this==ReturnFailure;
		}
	}
	public static enum WhenRolesNotFound {
		Continue,
		ReturnFailure,
		ReturnSuccess;
		public boolean returnFailure() {
			return this==ReturnFailure;
		}
		public boolean returnSuccess() {
			return this==ReturnSuccess;
		}
	}
	public static enum WhenRolesFound {
		Continue,
		ReturnSuccess;
		public boolean returnSuccess() {
			return this==ReturnSuccess;
		}
	}
}
