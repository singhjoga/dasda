package net.devoat.component.v1.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import net.devoat.common.Views;
import net.devoat.common.auditing.Auditable;
import net.devoat.common.domain.Deactivateable;
import net.devoat.common.domain.FunctionalId;
import net.devoat.common.validation.annotations.NotNullable;
import net.devoat.constants.Entities;

@Entity(name="COMP_TYPE")
public class ComponentType implements Auditable<String>, FunctionalId,Deactivateable{

	@Id
	@Column(name="TYPE_CODE")
	@JsonView(value= {Views.Add.class,Views.List.class})
	@NotNullable	
	private String code;
	@NotNullable	
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String name;
	@Column(name="IS_DISABLED")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isDisabled;
	@Column(name="IS_BUILDABLE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isBuildable;
	@Column(name="IS_DEPLOYABLE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isDeployable;
	@Column(name="IS_STARTABLE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isStartable;
	@Column(name="IS_STOPABLE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isStopable;
	@Column(name="IS_UNDEPLOYABLE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isUndeployable;
	@Column(name="IS_DELETEABLE")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean isDeleteable;
	@Column(name="ALLOWS_STAT_QUERY")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean allowsStatusQuery;
	@Column(name="ALLOWS_LOG_QUERY")
	@JsonView(value= {Views.Add.class,Views.List.class,Views.Update.class})
	@NotNullable
	private Boolean allowsLogQuery;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Boolean getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(Boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	public Boolean getIsDeployable() {
		return isDeployable;
	}
	public void setIsDeployable(Boolean isDeployable) {
		this.isDeployable = isDeployable;
	}
	public Boolean getIsStartable() {
		return isStartable;
	}
	public void setIsStartable(Boolean isStartable) {
		this.isStartable = isStartable;
	}
	public Boolean getIsStopable() {
		return isStopable;
	}
	public void setIsStopable(Boolean isStopable) {
		this.isStopable = isStopable;
	}
	public Boolean getIsUndeployable() {
		return isUndeployable;
	}
	public void setIsUndeployable(Boolean isUndeployable) {
		this.isUndeployable = isUndeployable;
	}
	public Boolean getIsDeleteable() {
		return isDeleteable;
	}
	public void setIsDeleteable(Boolean isDeleteable) {
		this.isDeleteable = isDeleteable;
	}
	public Boolean getAllowsStatusQuery() {
		return allowsStatusQuery;
	}
	public void setAllowsStatusQuery(Boolean allowsStatusQuery) {
		this.allowsStatusQuery = allowsStatusQuery;
	}
	public Boolean getAllowsLogQuery() {
		return allowsLogQuery;
	}
	public void setAllowsLogQuery(Boolean allowsLogQuery) {
		this.allowsLogQuery = allowsLogQuery;
	}
	public Boolean getIsBuildable() {
		return isBuildable;
	}
	public void setIsBuildable(Boolean isBuildable) {
		this.isBuildable = isBuildable;
	}
	@Override
	public String getObjectType() {
		return Entities.COMPONENT_TYPE;
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
