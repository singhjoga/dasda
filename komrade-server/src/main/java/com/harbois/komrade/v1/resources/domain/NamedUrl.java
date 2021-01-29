package com.harbois.komrade.v1.resources.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.oauth.api.v1.common.Views;

@Embeddable
public class NamedUrl {
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.View.class,Views.Update.class})
	@NotNullable
	private String name;
	@Column(name="URL")
	@JsonView(value= {Views.Add.class,Views.View.class,Views.Update.class})
	@NotNullable
	private String url;
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
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
