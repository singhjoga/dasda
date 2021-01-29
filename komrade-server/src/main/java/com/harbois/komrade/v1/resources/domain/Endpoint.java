package com.harbois.komrade.v1.resources.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.BaseAuditableEntity;
import com.harbois.oauth.api.v1.common.domain.Deactivateable;
import com.harbois.oauth.api.v1.common.domain.GeneratedUuid;

@Entity(name="RES_ENDPOINT")
public class Endpoint extends BaseAuditableEntity implements GeneratedUuid,Auditable<String>,Deactivateable{
	@Id
	@Column(name="ENDPOINT_ID")
	@JsonView(value= {Views.List.class,Views.Update.class})
	@UpdatePolicy(updateable=false,insertable=false)	
	private String id;
	@Column(name="RES_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String resourceCode;	
	@Column(name="ENV_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String envCode;
	@Column(name="PROVIDER_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String providerCode;
	@Column(name="IS_DISABLED")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isDisabled;
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="RES_ENDPOINT_CRED",joinColumns=@JoinColumn(name="ENDPOINT_ID"))
	@JsonView(value= {Views.Add.class,Views.View.class,Views.Update.class})
	@NotNullable
	private List<NamedCredentail> credentails;
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name="RES_ENDPOINT_URL",joinColumns=@JoinColumn(name="ENDPOINT_ID"))
	@JsonView(value= {Views.Add.class,Views.View.class,Views.Update.class})
	@NotNullable
	private List<NamedUrl> urls;
	
	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getEnvCode() {
		return envCode;
	}

	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
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
		return Entities.ENDPOINT;
	}

	public List<NamedCredentail> getCredentails() {
		return credentails;
	}

	public void setCredentails(List<NamedCredentail> credentails) {
		this.credentails = credentails;
	}

	public List<NamedUrl> getUrls() {
		return urls;
	}

	public void setUrls(List<NamedUrl> urls) {
		this.urls = urls;
	}

	@Override
	public String getName() {
		return envCode;
	}
}
