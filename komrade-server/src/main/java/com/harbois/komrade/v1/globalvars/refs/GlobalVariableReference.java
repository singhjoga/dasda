package com.harbois.komrade.v1.globalvars.refs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "GLOBAL_VAR_REF")
@IdClass(value=GlobalVariableReferenceId.class)
public class GlobalVariableReference {
	@Id
	@Column(name = "GLOBAL_VAR_ID")
	private String propertyId;
	@Id
	@Column(name = "ENTITY_TYPE")
	private String entityType;
	@Id
	@Column(name = "ENTITY_ID")
	private String entityId;
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
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	@Override
	public String toString() {
		return "GlobalPropertyReference [propertyId=" + propertyId + ", entityType=" + entityType + ", entityId=" + entityId + "]";
	}
	
}