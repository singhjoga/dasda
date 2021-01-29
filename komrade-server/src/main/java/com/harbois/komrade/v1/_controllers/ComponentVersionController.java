package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseChildEntityController;
import com.harbois.komrade.v1.component.version.ComponentVersion;
import com.harbois.komrade.v1.component.version.ComponentVersionService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/versions")
@Authorization(entity=Entities.COMPONENT_VERSION)
public class ComponentVersionController extends BaseChildEntityController<ComponentVersion, String, String> {
	@Autowired
	public ComponentVersionController(ComponentVersionService service) {
		super(service);
	}
}
