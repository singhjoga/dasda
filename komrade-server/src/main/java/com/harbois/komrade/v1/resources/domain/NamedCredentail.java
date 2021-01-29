package com.harbois.komrade.v1.resources.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.oauth.api.v1.common.Views;

@Embeddable
public class NamedCredentail {
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.View.class,Views.Update.class})
	@NotNullable
	private String name;
	@Column(name="CRED_ID")
	@JsonView(value= {Views.Add.class,Views.View.class,Views.Update.class})
	@NotNullable
	private String credentialsId;
	@Column(name="IS_DEFAULT")
	@JsonView(value= {Views.Add.class,Views.View.class,Views.Update.class})
	@NotNullable
	private Boolean isDefault;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCredentialsId() {
		return credentialsId;
	}
	public void setCredentialsId(String credentialsId) {
		this.credentialsId = credentialsId;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
}
