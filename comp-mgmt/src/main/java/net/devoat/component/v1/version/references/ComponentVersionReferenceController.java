package net.devoat.component.v1.version.references;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.devoat.common.controllers.BaseChildEntityController;
import net.devoat.constants.Entities;
import net.devoat.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/versions/references")
@Authorization(entity=Entities.COMPONENT_VERSION_REFERENCE)
public class ComponentVersionReferenceController extends BaseChildEntityController<ComponentVersionReference, String, String> {
	@Autowired
	public ComponentVersionReferenceController(ComponentVersionReferenceService service) {
		super(service);
	}
}
