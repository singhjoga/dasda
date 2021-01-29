package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.environment.TargetEnvironment;
import com.harbois.komrade.v1.environment.TargetEnvironmentService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/environments")
@Authorization(entity=Entities.TARGET_ENVIRONMENT)
public class TargetEnvironmentController extends BaseEntityController<TargetEnvironment, String> {
	@Autowired
	public TargetEnvironmentController(TargetEnvironmentService service) {
		super(service);
	}
}
