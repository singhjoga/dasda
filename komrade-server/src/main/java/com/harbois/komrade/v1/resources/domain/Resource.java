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
import com.harbois.oauth.api.v1.common.domain.FunctionalId;

@Entity(name="RES")
public class Resource extends BaseAuditableEntity implements FunctionalId,Auditable<String>,Deactivateable{
	@Column(name="RES_CODE")
	@Id
	@JsonView(value= {Views.Add.class,Views.List.class})
	@NotNullable
	@UpdatePolicy(updateable=false)	
	private String code;
	
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String name;

	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String description;
	
	@Column(name="RES_TYPE_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@NotNullable
	private String typeCode;
	
	@Column(name="TEAM_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable	
	private String teamCode;

	@Column(name="PROCESS_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String processCode;
	
	@Column(name="IS_DISABLED")
	@JsonView(value= {Views.List.class,Views.Update.class})
	private Boolean isDisabled;

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

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}


	public Boolean getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
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
		return Entities.RESOURCE;
	}
}
