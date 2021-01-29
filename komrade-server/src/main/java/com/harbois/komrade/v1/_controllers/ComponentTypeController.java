package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.component.type.ComponentType;
import com.harbois.komrade.v1.component.type.ComponentTypeService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/component-types")
@Authorization(entity=Entities.COMPONENT_TYPE)
public class ComponentTypeController extends BaseEntityController<ComponentType, String> {
	@Autowired
	public ComponentTypeController(ComponentTypeService service) {
		super(service);
	}
}
