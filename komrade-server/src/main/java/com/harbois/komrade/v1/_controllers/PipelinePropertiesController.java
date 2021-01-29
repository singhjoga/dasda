package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEnvPropertiesChildController;
import com.harbois.komrade.v1.pipelines.PipelinePropertyService;
import com.harbois.komrade.v1.pipelines.domain.PipelineProperty;
import com.harbois.komrade.v1.pipelines.domain.PipelinePropertyValue;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/pipelines/properties")
@Authorization(entity=Entities.PIPELINE_PROPERTY)
public class PipelinePropertiesController extends BaseEnvPropertiesChildController<PipelineProperty, PipelinePropertyValue> {
	@Autowired
	public PipelinePropertiesController(PipelinePropertyService service) {
		super(service);
	}

}
