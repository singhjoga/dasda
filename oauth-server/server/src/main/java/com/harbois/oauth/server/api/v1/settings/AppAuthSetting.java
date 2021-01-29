package com.harbois.oauth.server.api.v1.settings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.harbois.oauth.server.api.v1.common.domain.ClientIdentity;
import com.harbois.oauth.server.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.server.validation.annotations.NotNullable;
import com.harbois.oauth.server.validation.annotations.UpdatePolicy;

@Entity(name="APP_AUTH_SETTING")
public class AppAuthSetting implements IdentifiableEntity<Long>, ClientIdentity{
	@Id
	@TableGenerator(name = "SETTINGS_ID", table= "APP_ID_GEN", initialValue = 100, allocationSize = 10)
    @GeneratedValue(generator = "SETTINGS_ID")
	@Column(name="SETTINGS_ID")
	@UpdatePolicy(insertable=false, updateable=false)	
	private Long id;

	@NotNullable	
	@Column(name="DISPLAY_ORDER")
	private Float displayOrder;
	
	@Column(name="NAME")
	@NotNullable
	private String name;

	@Column(name="DESCRIPTION")
	@NotNullable
	private String description;
		
	@Column(name="PROVIDER_TYPE")
	@NotNullable
	@Enumerated(EnumType.STRING)
	private AuthProviderType providerType;
	
	@Column(name="CLIENT_ID")
	@NotNullable
	private String clientId;
	
	@Column(name="IS_DISABLED")
	private Boolean isDisabled;
	
	@Column(name="SETTINGS_JSON")
	@NotNullable
	private String settingsJson;

	public Float getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Float displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AuthProviderType getProviderType() {
		return providerType;
	}

	public void setProviderType(AuthProviderType providerType) {
		this.providerType = providerType;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public String getSettingsJson() {
		return settingsJson;
	}

	public void setSettingsJson(String settingsJson) {
		this.settingsJson = settingsJson;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id=id;
	}

}
