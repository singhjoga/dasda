package com.harbois.oauth.server.authentication.ldap;

import com.harbois.oauth.server.authentication.AuthSettings;

public class LdapAuthSettings extends AuthSettings{
	private String ldapUrl;
	private String useTls;
	private String userDnPattern;
	private String userSearchBase;
	private String userSearchFilter;
	private String groupSearchBase;
	private String groupSearchFilter;
	private String groupRoleAttribute="cn";
	private String managerDn;
	private String managerPassword;
	
	public String getLdapUrl() {
		return ldapUrl;
	}
	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}
	public String getUseTls() {
		return useTls;
	}
	public void setUseTls(String useTls) {
		this.useTls = useTls;
	}
	public String getUserDnPattern() {
		return userDnPattern;
	}
	public void setUserDnPattern(String userDnPattern) {
		this.userDnPattern = userDnPattern;
	}
	public String getUserSearchBase() {
		return userSearchBase;
	}
	public void setUserSearchBase(String userSearchBase) {
		this.userSearchBase = userSearchBase;
	}
	public String getUserSearchFilter() {
		return userSearchFilter;
	}
	public void setUserSearchFilter(String userSearchFilter) {
		this.userSearchFilter = userSearchFilter;
	}
	public String getGroupSearchBase() {
		return groupSearchBase;
	}
	public void setGroupSearchBase(String groupSearchBase) {
		this.groupSearchBase = groupSearchBase;
	}
	public String getGroupSearchFilter() {
		return groupSearchFilter;
	}
	public void setGroupSearchFilter(String groupSearchFilter) {
		this.groupSearchFilter = groupSearchFilter;
	}
	public String getManagerDn() {
		return managerDn;
	}
	public void setManagerDn(String managerDn) {
		this.managerDn = managerDn;
	}
	public String getManagerPassword() {
		return managerPassword;
	}
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	public String getGroupRoleAttribute() {
		return groupRoleAttribute;
	}
	public void setGroupRoleAttribute(String groupRoleAttribute) {
		this.groupRoleAttribute = groupRoleAttribute;
	}
	
}
