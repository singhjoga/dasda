package com.harbois.komrade.v1.refdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.BaseAuditableEntity;
import com.harbois.oauth.api.v1.common.domain.Deactivateable;
import com.harbois.oauth.api.v1.common.domain.FunctionalId;

@Entity(name="REF_DATA_USR")
public class RefDataUser extends BaseAuditableEntity implements Auditable<String>,FunctionalId,Deactivateable{

	@Id
	@Column(name="REF_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@UpdatePolicy(updateable=false)
	private String code;

	@Column(name="REF_TYPE")
	@JsonView(value= {Views.Add.class,Views.List.class})
	private String referenceType;
	
	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String description;

	@Column(name="IS_DISABLED")
	@JsonView(value= {Views.Update.class,Views.List.class})
	private Boolean isDisabled;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	@Override
	public String getId() {
		return code;
	}
	@Override
	public void setId(String id) {
		this.code=id;
	}
	@Override
	public String getObjectType() {
		return Entities.REF_DATA;
	}
	@Override
	public String getName() {
		return code;
	}

}
