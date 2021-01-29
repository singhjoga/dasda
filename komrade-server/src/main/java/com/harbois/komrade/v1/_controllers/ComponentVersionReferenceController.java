package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseChildEntityController;
import com.harbois.komrade.v1.component.version.references.ComponentVersionReference;
import com.harbois.komrade.v1.component.version.references.ComponentVersionReferenceService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/versions/references")
@Authorization(entity=Entities.COMPONENT_VERSION_REFERENCE)
public class ComponentVersionReferenceController extends BaseChildEntityController<ComponentVersionReference, String, String> {
	@Autowired
	public ComponentVersionReferenceController(ComponentVersionReferenceService service) {
		super(service);
	}
}
