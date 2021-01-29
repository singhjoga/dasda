package com.harbois.komrade.v1.system.properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.properties.PropertyWithValue;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.FunctionalId;

@Entity(name="SYS_PROP")
public class SystemProperty implements PropertyWithValue<SystemProperty>, FunctionalId,Auditable<String>{

	@Id
	@Column(name="NAME")
	@JsonView(value= {Views.List.class})
	@UpdatePolicy(updateable=false,insertable=false)
	private String name;
	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.List.class})	
	@UpdatePolicy(updateable=false,insertable=false)
	private String description;
	@Column(name="CAT")
	@JsonView(value= {Views.List.class})	
	@UpdatePolicy(updateable=false,insertable=false)
	private String category; 
	@Column(name="TYPE_CODE")
	@JsonView(value= {Views.List.class})	
	@UpdatePolicy(updateable=false,insertable=false)
	private String typeCode; 
	@Column(name="VALUE")
	@JsonView(value= {Views.List.class, Views.Update.class})	
	@UpdatePolicy(insertable=false)
	private String value;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	@JsonIgnore
	public String getId() {
		return name;
	}
	@Override
	public void setId(String id) {
		this.name=id;
	}
	@Override
	public String getObjectType() {
		return Entities.SYSTEM_PROPERTIES;
	}

	public SystemProperty create() {
		return new SystemProperty();
	}

	public SystemProperty copy() {
		SystemProperty copy = create();
		copyTo(copy);
		return copy;
	}

	public SystemProperty copyTo(SystemProperty copyTo) {
		copyTo.setDescription(getDescription());
		copyTo.setName(getName());
		copyTo.setTypeCode(getTypeCode());
		copyTo.setValue(getValue());
		copyTo.setCategory(getCategory());
		return copyTo;
	}
}
