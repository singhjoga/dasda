package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseChildEntityController;
import com.harbois.komrade.v1.component.version.actions.ComponentVersionAction;
import com.harbois.komrade.v1.component.version.actions.ComponentVersionActionService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/versions/actions")
@Authorization(entity=Entities.COMPONENT_VERSION_ACTION)
public class ComponentVersionActionController extends BaseChildEntityController<ComponentVersionAction, String, String> {
	@Autowired
	public ComponentVersionActionController(ComponentVersionActionService service) {
		super(service);
	}
}
