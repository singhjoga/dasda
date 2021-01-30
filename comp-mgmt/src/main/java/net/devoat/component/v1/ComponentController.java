package net.devoat.component.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.devoat.common.controllers.BaseEntityController;
import net.devoat.constants.Entities;
import net.devoat.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/components")
@Authorization(entity=Entities.COMPONENT)
public class ComponentController extends BaseEntityController<Component, String> {
	@Autowired
	public ComponentController(ComponentService service) {
		super(service);
	}
}
