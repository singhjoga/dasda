package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.resources.EndpointService;
import com.harbois.komrade.v1.resources.domain.Endpoint;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/endpoints")
@Authorization(entity=Entities.HOST)
public class EndpointsController extends BaseEntityController<Endpoint, String> {
	@Autowired
	public EndpointsController(EndpointService service) {
		super(service);
	}
}
