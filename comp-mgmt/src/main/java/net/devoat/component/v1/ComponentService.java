package net.devoat.component.v1;

import org.springframework.beans.factory.annotation.Autowired;

import net.devoat.common.services.BaseEntityService;

@org.springframework.stereotype.Component
public class ComponentService extends BaseEntityService<Component, String>{
	@Autowired	
	public ComponentService(ComponentRepository repo) {
		super(repo,Component.class, String.class);
	}
}
