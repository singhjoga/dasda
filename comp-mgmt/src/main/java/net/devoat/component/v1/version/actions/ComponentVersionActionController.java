package net.devoat.component.v1.version.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.devoat.common.controllers.BaseChildEntityController;
import net.devoat.constants.Entities;
import net.devoat.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components/versions/actions")
@Authorization(entity=Entities.COMPONENT_VERSION_ACTION)
public class ComponentVersionActionController extends BaseChildEntityController<ComponentVersionAction, String, String> {
	@Autowired
	public ComponentVersionActionController(ComponentVersionActionService service) {
		super(service);
	}
}
