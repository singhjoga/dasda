package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.resources.ResourceService;
import com.harbois.komrade.v1.resources.domain.Resource;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/resources")
@Authorization(entity=Entities.RESOURCE)
public class ResourcesController extends BaseEntityController<Resource, String> {
	@Autowired
	public ResourcesController(ResourceService service) {
		super(service);
	}
}
