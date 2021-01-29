package com.harbois.oauth.authentication.ad;

import com.harbois.oauth.authentication.AuthSettings;

public class AdAuthSettings extends AuthSettings{
	private String ldapUrl;
	private String domainName;
	private String managerDn;
	private String managerPassword;
	private String searchBase;
	private String searchFilter;
	public String getLdapUrl() {
		return ldapUrl;
	}
	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}

	public String getManagerPassword() {
		return managerPassword;
	}
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	public String getSearchBase() {
		return searchBase;
	}
	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase;
	}
	public String getSearchFilter() {
		return searchFilter;
	}
	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}

	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getManagerDn() {
		return managerDn;
	}
	public void setManagerDn(String managerDn) {
		this.managerDn = managerDn;
	}
}
