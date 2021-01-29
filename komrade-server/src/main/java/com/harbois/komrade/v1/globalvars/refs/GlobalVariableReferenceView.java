package com.harbois.komrade.v1.globalvars.refs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "GLOBAL_PROP_REF_VIEW")
@IdClass(value=GlobalVariableReferenceId.class)
public class GlobalVariableReferenceView {
	@Column(name = "GLOBAL_PROP_ID")
	private String propertyId;
	@Id
	@Column(name = "ENTITY_TYPE")
	private String entityType;
	@Id
	@Column(name = "ENTITY_ID")
	private Long entityId;
	@Column(name = "PARENT_ENTITY_NAME")
	private String parentEntityName;
	@Column(name = "PARENT_ENTITY_ID")
	private Long parentEntityId;
	@Column(name = "PROP_NAME")
	private String propertyName;
	@Column(name = "VALUE")
	private String value;
	public String getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public String getParentEntityName() {
		return parentEntityName;
	}
	public void setParentEntityName(String parentEntityName) {
		this.parentEntityName = parentEntityName;
	}
	public Long getParentEntityId() {
		return parentEntityId;
	}
	public void setParentEntityId(Long parentEntityId) {
		this.parentEntityId = parentEntityId;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}