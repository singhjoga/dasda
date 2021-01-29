package com.harbois.komrade.v1.pipelines.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;

@Entity(name="PIPELINE_PROP_VAL")
@AttributeOverrides({
	@AttributeOverride(name="id",column=@Column(name="PIPELINE_PROP_VAL_ID")),
	@AttributeOverride(name="propertyId",column=@Column(name="PIPELINE_PROP_ID"))
})
public class PipelinePropertyValue extends PersistentPropertyEnvBasedValue<PipelinePropertyValue>{
	@Override
	public PipelinePropertyValue create() {
		return new PipelinePropertyValue();
	}
}
