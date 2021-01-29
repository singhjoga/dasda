package com.harbois.komrade.v1.common.properties;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.BaseAuditableEntity;
import com.harbois.oauth.api.v1.common.domain.GeneratedUuid;

@MappedSuperclass
public abstract class PersistentProperty<T extends PersistentProperty<T>> extends BaseAuditableEntity implements TypedProperty<T>, GeneratedUuid{
	@Column(name="OVERRIDE_IT_IN_CONCRETE_CLASS")
	@Id
	@JsonView(value= {Views.List.class})
	private String id;
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String name;
	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private String description;
	@Column(name="CAT")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	private String category; 
	@Column(name="TYPE_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private String typeCode; 
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String getTypeCode() {
		return typeCode;
	}
	@Override
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public abstract T create();
	public T copy() {
		T copy = create();
		copyTo(copy);
		return copy;
	}
	public T copyTo(T copyTo) {
		copyTo.setDescription(getDescription());
		copyTo.setName(getName());
		copyTo.setTypeCode(getTypeCode());
		copyTo.setCategory(getCategory());
		return copyTo;
	}
}