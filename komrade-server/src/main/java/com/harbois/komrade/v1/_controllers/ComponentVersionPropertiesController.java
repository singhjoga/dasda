package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEnvPropertiesChildController;
import com.harbois.komrade.v1.component.version.properties.ComponentVersionProperty;
import com.harbois.komrade.v1.component.version.properties.ComponentVersionPropertyService;
import com.harbois.komrade.v1.component.version.properties.ComponentVersionPropertyValue;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/versions/properties")
@Authorization(entity=Entities.COMPONENT_VERSION_PROPERTY)
public class ComponentVersionPropertiesController extends BaseEnvPropertiesChildController<ComponentVersionProperty, ComponentVersionPropertyValue> {
	@Autowired
	public ComponentVersionPropertiesController(ComponentVersionPropertyService service) {
		super(service);
	}

}
