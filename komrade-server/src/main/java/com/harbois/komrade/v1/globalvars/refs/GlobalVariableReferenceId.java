package com.harbois.komrade.v1.globalvars.refs;

import java.io.Serializable;

import javax.persistence.Column;

public class GlobalVariableReferenceId implements Serializable{

	private static final long serialVersionUID = 2267727773669530935L;
	@Column(name = "GLOBAL_PROP_ID")
	private String propertyId;
	@Column(name="ENTITY_TYPE")
	private String entityType;
	@Column(name="ENTITY_ID")
	private String entityId;
	
	public GlobalVariableReferenceId() {
		super();
	}
	public GlobalVariableReferenceId(String entityType, String entityId) {
		super();
		this.entityType = entityType;
		this.entityId = entityId;
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
	public String getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
	
}
