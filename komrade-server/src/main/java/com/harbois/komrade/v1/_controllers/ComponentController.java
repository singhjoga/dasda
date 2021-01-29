package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.component.Component;
import com.harbois.komrade.v1.component.ComponentService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components")
@Authorization(entity=Entities.COMPONENT)
public class ComponentController extends BaseEntityController<Component, String> {
	@Autowired
	public ComponentController(ComponentService service) {
		super(service);
	}
}
