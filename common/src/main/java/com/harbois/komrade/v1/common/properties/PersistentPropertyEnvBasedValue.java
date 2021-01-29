package com.harbois.komrade.v1.common.properties;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.GeneratedUuid;

@MappedSuperclass
public abstract class PersistentPropertyEnvBasedValue<T extends PersistentPropertyEnvBasedValue<T>> implements PropertyValue<T>, GeneratedUuid{
	@Column(name="OVERRIDE_IT_IN_CONCRETE_CLASS")
	@Id
	@JsonView(value= {Views.List.class})
	private String id;
	@Column(name="OVERRIDE_IT_IN_CONCRETE_CLASS")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@NotNullable
	private String propertyId;
	@Column(name="ENV_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@NotNullable
	private String envCode;
	@Column(name="VALUE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private String value;
	
	@Transient
	private Boolean isInherited=false;
	
	@Override
	public abstract T create();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
	public String getEnvCode() {
		return envCode;
	}
	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getIsInherited() {
		return isInherited;
	}
	public void setIsInherited(Boolean isInherited) {
		this.isInherited = isInherited;
	}
	@Override
	public T copy() {
		T copy = create();
		copyTo(copy);
		return copy;
	}
	@Override
	public T copyTo(T copyTo) {
		copyTo.setPropertyId(getPropertyId());
		copyTo.setEnvCode(getEnvCode());
		copyTo.setValue(getValue());
		return copyTo;
	}
}