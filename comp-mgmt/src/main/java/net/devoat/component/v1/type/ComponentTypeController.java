package net.devoat.component.v1.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.devoat.common.controllers.BaseEntityController;
import net.devoat.constants.Entities;
import net.devoat.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/component-types")
@Authorization(entity=Entities.COMPONENT_TYPE)
public class ComponentTypeController extends BaseEntityController<ComponentType, String> {
	@Autowired
	public ComponentTypeController(ComponentTypeService service) {
		super(service);
	}
}
