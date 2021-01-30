package net.devoat.component.v1.version;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import net.devoat.common.Views;
import net.devoat.common.auditing.Auditable;
import net.devoat.common.domain.BaseAuditableEntity;
import net.devoat.common.domain.GeneratedUuid;
import net.devoat.common.domain.StatusChangeable;
import net.devoat.common.validation.annotations.NotNullable;
import net.devoat.common.validation.annotations.UpdatePolicy;
import net.devoat.constants.Entities;

@Entity(name="COMP_VER")
public class ComponentVersion extends BaseAuditableEntity implements Auditable<String>, GeneratedUuid,StatusChangeable{
	@Id
	@Column(name="COMP_VER_ID")
	@JsonView(value= {Views.List.class})
	@UpdatePolicy(updateable=false,insertable=false)
	private String id;
	@Column(name="COMP_ID")
	@JsonView(value= {Views.List.class,Views.Add.class})
	@NotNullable
	private String componentId;	
	@NotNullable	
	@Column(name="VERSION")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String version;
	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String description;
	@Column(name="STATUS")
	@JsonView(value= {Views.List.class})
	private String status;
	@Column(name="RELEASE_NOTES")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String releaseNotes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReleaseNotes() {
		return releaseNotes;
	}
	public void setReleaseNotes(String releaseNotes) {
		this.releaseNotes = releaseNotes;
	}
	@Override
	public String getObjectType() {
		return Entities.COMPONENT_VERSION;
	}
	@Override
	public String getName() {
		return version;
	}
}
