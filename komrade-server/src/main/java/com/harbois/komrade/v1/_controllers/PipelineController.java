package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.pipelines.PipelineService;
import com.harbois.komrade.v1.pipelines.domain.Pipeline;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/pipelines")
@Authorization(entity=Entities.PIPELINE)
public class PipelineController extends BaseEntityController<Pipeline, String> {
	@Autowired
	public PipelineController(PipelineService service) {
		super(service);
	}
}
