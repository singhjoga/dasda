package com.harbois.komrade.v1.environment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.FunctionalId;

@Entity(name="TARGET_ENV")
public class TargetEnvironment implements Auditable<String>, FunctionalId{

	@Id
	@Column(name="ENV_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@NotNullable	
	@UpdatePolicy(updateable=false)
	private String code;
	@NotNullable	
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String name;
	@Column(name="DISPLAY_ORDER")
	@NotNullable
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private Float displayOrder;	
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Float displayOrder) {
		this.displayOrder = displayOrder;
	}
	@Override
	public String getObjectType() {
		return Entities.TARGET_ENVIRONMENT;
	}
	@Override
	public void setId(String id) {
		this.code=id;
	}
	@Override
	public String getId() {
		return code;
	}
}
