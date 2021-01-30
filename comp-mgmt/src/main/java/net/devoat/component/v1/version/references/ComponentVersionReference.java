package net.devoat.component.v1.version.references;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import net.devoat.common.Views;
import net.devoat.common.auditing.Auditable;
import net.devoat.common.domain.BaseAuditableEntity;
import net.devoat.common.domain.GeneratedUuid;
import net.devoat.common.validation.annotations.NotNullable;
import net.devoat.common.validation.annotations.UpdatePolicy;
import net.devoat.constants.Entities;

@Entity(name="COMP_VER_REF")
public class ComponentVersionReference extends BaseAuditableEntity implements Auditable<String>, GeneratedUuid{
	@Id
	@Column(name="COMP_VER_REF_ID")
	@JsonView(value= {Views.List.class})
	@UpdatePolicy(updateable=false,insertable=false)
	private String id;
	@Column(name="COMP_VER_ID")
	@JsonView(value= {Views.List.class,Views.Add.class})
	@NotNullable
	private String componentVersionId;	
	@NotNullable
	@Column(name="SYS_CODE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String systemCode;
	@Column(name="refTypeCode")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	@NotNullable
	private String referenceTypeCode;
	@Column(name="REF")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	@NotNullable
	private String reference;
	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String description;	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComponentVersionId() {
		return componentVersionId;
	}
	public void setComponentVersionId(String componentVersionId) {
		this.componentVersionId = componentVersionId;
	}
	public String getSystemCode() {
		return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getReferenceTypeCode() {
		return referenceTypeCode;
	}
	public void setReferenceTypeCode(String referenceTypeCode) {
		this.referenceTypeCode = referenceTypeCode;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String getObjectType() {
		return Entities.COMPONENT_VERSION;
	}
	@Override
	public String getName() {
		return reference;
	}
}
