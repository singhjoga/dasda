package com.harbois.komrade.v1.pipelines.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.komrade.constants.Entities;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.BaseAuditableEntity;
import com.harbois.oauth.api.v1.common.domain.GeneratedUuid;

@Entity(name="PIPELINE")
public class Pipeline extends BaseAuditableEntity implements Auditable<String>, GeneratedUuid{
	@Id
	@Column(name="PIPELINE_ID")
	@JsonView(value= {Views.List.class})
	@UpdatePolicy(updateable=false,insertable=false)
	private String id;
	@NotNullable	
	@Column(name="NAME")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String name;
	@NotNullable	
	@Column(name="DESCRIPTION")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String description;
	@Column(name="TEAM_CODE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String teamCode;
	@Column(name="COMP_TYPE_CODE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String componentTypeCode;
	@Column(name="PARENT_ID")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String parentId;
	@Column(name="IS_ABSTRACT")
	@JsonView(value= {Views.List.class,Views.Update.class})
	private Boolean isAbstract;		
	@Column(name="BUILD_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String buildPipeLine;
	@Column(name="DEPLOY_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String deployPipeLine;
	@Column(name="START_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String startPipeLine;
	@Column(name="STOP_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String stopPipeLine;
	@Column(name="UNDEPLOY_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String undeployPipeLine;
	@Column(name="DELETE_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String deletePipeLine;
	@Column(name="QUERY_LOG_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String queryLogPipeLine;
	@Column(name="QUERY_STATUS_PIPELINE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String queryStatusPipeLine;
	@Override
	public String getObjectType() {
		return Entities.PIPELINE;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	public String getComponentTypeCode() {
		return componentTypeCode;
	}
	public void setComponentTypeCode(String componentTypeCode) {
		this.componentTypeCode = componentTypeCode;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Boolean getIsAbstract() {
		return isAbstract;
	}
	public void setIsAbstract(Boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	public String getBuildPipeLine() {
		return buildPipeLine;
	}
	public void setBuildPipeLine(String buildPipeLine) {
		this.buildPipeLine = buildPipeLine;
	}
	public String getDeployPipeLine() {
		return deployPipeLine;
	}
	public void setDeployPipeLine(String deployPipeLine) {
		this.deployPipeLine = deployPipeLine;
	}
	public String getStartPipeLine() {
		return startPipeLine;
	}
	public void setStartPipeLine(String startPipeLine) {
		this.startPipeLine = startPipeLine;
	}
	public String getStopPipeLine() {
		return stopPipeLine;
	}
	public void setStopPipeLine(String stopPipeLine) {
		this.stopPipeLine = stopPipeLine;
	}
	public String getUndeployPipeLine() {
		return undeployPipeLine;
	}
	public void setUndeployPipeLine(String undeployPipeLine) {
		this.undeployPipeLine = undeployPipeLine;
	}
	public String getDeletePipeLine() {
		return deletePipeLine;
	}
	public void setDeletePipeLine(String deletePipeLine) {
		this.deletePipeLine = deletePipeLine;
	}
	public String getQueryLogPipeLine() {
		return queryLogPipeLine;
	}
	public void setQueryLogPipeLine(String queryLogPipeLine) {
		this.queryLogPipeLine = queryLogPipeLine;
	}
	public String getQueryStatusPipeLine() {
		return queryStatusPipeLine;
	}
	public void setQueryStatusPipeLine(String queryStatusPipeLine) {
		this.queryStatusPipeLine = queryStatusPipeLine;
	}

}
