package com.harbois.komrade.v1.pipelines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEnvPropertiesChildService;
import com.harbois.komrade.v1.pipelines.domain.PipelineProperty;
import com.harbois.komrade.v1.pipelines.domain.PipelinePropertyValue;

@Component
public class PipelinePropertyService extends BaseEnvPropertiesChildService<PipelineProperty, PipelinePropertyValue>{
	
	@Autowired
	public PipelinePropertyService(PipelinePropertyRepository repo, PipelinePropertyValueRepository valueRepo) {
		super(repo,PipelineProperty.class, valueRepo, PipelinePropertyValue.class);
	}
}
