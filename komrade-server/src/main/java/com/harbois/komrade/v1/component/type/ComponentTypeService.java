package com.harbois.komrade.v1.component.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;

@Component
public class ComponentTypeService extends BaseEntityService<ComponentType, String>{
	@Autowired	
	public ComponentTypeService(ComponentTypeRepository repo) {
		super(repo,ComponentType.class, String.class);
	}
}
