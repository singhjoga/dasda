package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEnvPropertiesChildController;
import com.harbois.komrade.v1.component.properties.ComponentProperty;
import com.harbois.komrade.v1.component.properties.ComponentPropertyService;
import com.harbois.komrade.v1.component.properties.ComponentPropertyValue;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/properties")
@Authorization(entity=Entities.COMPONENT_PROPERTY)
public class ComponentPropertiesController extends BaseEnvPropertiesChildController<ComponentProperty, ComponentPropertyValue> {
	@Autowired
	public ComponentPropertiesController(ComponentPropertyService service) {
		super(service);
	}

}
