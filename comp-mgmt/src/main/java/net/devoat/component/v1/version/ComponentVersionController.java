package net.devoat.component.v1.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.devoat.common.controllers.BaseChildEntityController;
import net.devoat.constants.Entities;
import net.devoat.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/versions")
@Authorization(entity=Entities.COMPONENT_VERSION)
public class ComponentVersionController extends BaseChildEntityController<ComponentVersion, String, String> {
	@Autowired
	public ComponentVersionController(ComponentVersionService service) {
		super(service);
	}
}
