package com.harbois.komrade.v1.pipelines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;
import com.harbois.komrade.v1.pipelines.domain.Pipeline;

@Component
public class PipelineService extends BaseEntityService<Pipeline, String>{
	@Autowired	
	public PipelineService(PipelineRepository repo) {
		super(repo,Pipeline.class, String.class);
	}
}
