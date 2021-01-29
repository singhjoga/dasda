package com.harbois.komrade.v1.resources.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.BaseAuditableEntity;
import com.harbois.oauth.api.v1.common.domain.Deactivateable;
import com.harbois.oauth.api.v1.common.domain.GeneratedUuid;

@Entity(name="RES_HOST")
public class Host extends BaseAuditableEntity implements GeneratedUuid,Auditable<String>,Deactivateable{
	@Id
	@Column(name="RES_ID")
	@JsonView(value= {Views.List.class,Views.Update.class})
	@UpdatePolicy(updateable=false,insertable=false)	
	private String id;
	@Column(name="RES_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String resourceCode;
	
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String name;
	@Column(name="LOCATION_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String locationCode;

	@Column(name="OS_TYPE_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String osTypeCode;
	
	@Column(name="ENV_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String envCode;

	@Column(name="OS")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private String os;
	@Column(name="FQDN")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private String fqdn;
	@Column(name="DNS_ALIASES")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private String dnsAliases;
	@Column(name="CONN_CRED_ID")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String credentialsId;
	@Column(name="SU_CRED_ID")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private String suCredentialsId;
	@Column(name="IS_DISABLED")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private Boolean isDisabled;

	
	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getOsTypeCode() {
		return osTypeCode;
	}

	public void setOsTypeCode(String osTypeCode) {
		this.osTypeCode = osTypeCode;
	}

	public String getEnvCode() {
		return envCode;
	}

	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getDnsAliases() {
		return dnsAliases;
	}

	public void setDnsAliases(String dnsAliases) {
		this.dnsAliases = dnsAliases;
	}

	public String getCredentialsId() {
		return credentialsId;
	}

	public void setCredentialsId(String credentialsId) {
		this.credentialsId = credentialsId;
	}

	public String getSuCredentialsId() {
		return suCredentialsId;
	}

	public void setSuCredentialsId(String suCredentialsId) {
		this.suCredentialsId = suCredentialsId;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id=id;
	}

	@Override
	public String getObjectType() {
		return Entities.HOST;
	}
}
