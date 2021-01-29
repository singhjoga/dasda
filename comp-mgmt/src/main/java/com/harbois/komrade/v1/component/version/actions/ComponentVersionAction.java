package com.harbois.komrade.v1.component.version.actions;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.validation.annotations.NotNullable;
import com.harbois.common.validation.annotations.UpdatePolicy;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.GeneratedUuid;

@Entity(name="COMP_VER_ACT")
public class ComponentVersionAction implements GeneratedUuid{
	@Id
	@Column(name="COMP_VER_ACT_ID")
	@JsonView(value= {Views.List.class})
	@UpdatePolicy(updateable=false,insertable=false)
	private String id;
	@Column(name="COMP_VER_ID")
	@JsonView(value= {Views.List.class,Views.Add.class})
	@NotNullable
	private String componentVersionId;	
	@NotNullable
	@Column(name="ACTION")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private String action;
	@Column(name="ACTION_DATE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	@NotNullable
	private Date actionDate;
	@Column(name="STATUS")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	@NotNullable
	private String status;
	@Column(name="ENV_CODE")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	@NotNullable
	private String envCode;
	@Column(name="ACTION_USER")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	@NotNullable
	private String user;
	@Column(name="JOB_ID")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	@NotNullable
	private String jobId;
	@Column(name="IS_MANUAL")
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private Boolean isManual=false;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getActionDate() {
		return actionDate;
	}
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEnvCode() {
		return envCode;
	}
	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public Boolean getIsManual() {
		return isManual;
	}
	public void setIsManual(Boolean isManual) {
		this.isManual = isManual;
	}	
}
