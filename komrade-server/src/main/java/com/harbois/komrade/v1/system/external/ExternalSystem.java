package com.harbois.komrade.v1.system.external;

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
import com.harbois.oauth.api.v1.common.domain.FunctionalId;

@Entity(name="EXT_SYS")
public class ExternalSystem extends BaseAuditableEntity implements FunctionalId,Auditable<String>,Deactivateable{
	@Id
	@Column(name="SYS_CODE")
	@JsonView(value= {Views.List.class,Views.Add.class})
	@UpdatePolicy(updateable=false)
	private String code;
	@Column(name="NAME")
	@JsonView(value= {Views.List.class,Views.Add.class,Views.Update.class})
	@NotNullable
	private String name;
	@Column(name="SYS_TYPE_CODE")
	@JsonView(value= {Views.List.class,Views.Add.class,Views.Update.class})
	@NotNullable
	private String systemTypeCode;
	@Column(name="PROVIDER_CODE")
	@JsonView(value= {Views.List.class,Views.Add.class,Views.Update.class})
	@NotNullable
	private String providerCode;
	@Column(name="SETTINGS")
	@JsonView(value= {Views.List.class,Views.Add.class,Views.Update.class})
	private String settings;
	@Column(name="IS_DISABLED")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private Boolean isDisabled;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystemTypeCode() {
		return systemTypeCode;
	}

	public void setSystemTypeCode(String systemTypeCode) {
		this.systemTypeCode = systemTypeCode;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	@Override
	public String getObjectType() {
		return Entities.EXTERNAL_SYSTEM;
	}

	public ExternalSystem create() {
		return new ExternalSystem();
	}

	public ExternalSystem copy() {
		ExternalSystem copy = create();
		copyTo(copy);
		return copy;
	}

	public ExternalSystem copyTo(ExternalSystem copyTo) {
		copyTo.setName(getName());
		copyTo.setProviderCode(getProviderCode());
		copyTo.setSettings(getSettings());
		copyTo.setSystemTypeCode(getSystemTypeCode());
		return copyTo;
	}

	@Override
	public String getId() {
		return code;
	}

	@Override
	public void setId(String id) {
		this.code=id;
	}
}
