package com.harbois.komrade.v1.pipelines.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.Auditable;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.oauth.api.v1.common.Views;

@Entity(name="PIPELINE_PROP")
@AttributeOverride(name="id",column=@Column(name="PIPELINE_PROP_ID"))
public class PipelineProperty extends PersistentPropertyEnvBased<PipelineProperty, PipelinePropertyValue> implements Auditable<String>{
	@Column(name="IS_ENV_BASED")
	@JsonView(value= {Views.List.class,Views.Add.class})
	private Boolean isEnvBased;	
	@Column(name="PIPELINE_ID")
	@JsonView(value= {Views.List.class,Views.Add.class})
	private String pipelineId;	
	
	@Override
	public PipelineProperty copyTo(PipelineProperty copyTo) {
		copyTo.setIsEnvBased(isEnvBased);
		return super.copyTo(copyTo);
	}
	public Boolean getIsEnvBased() {
		return isEnvBased;
	}
	public void setIsEnvBased(Boolean isEnvBased) {
		this.isEnvBased = isEnvBased;
	}
	public String getPipelineId() {
		return pipelineId;
	}
	public void setPipelineId(String pipelineId) {
		this.pipelineId = pipelineId;
	}
	@Override
	public PipelineProperty create() {
		return new PipelineProperty();
	}

	@Override
	public String getObjectType() {
		return Entities.PIPELINE_PROPERTY;
	}

}
