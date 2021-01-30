package net.devoat.component.v1.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.devoat.common.services.BaseEntityService;

@Component
public class ComponentTypeService extends BaseEntityService<ComponentType, String>{
	@Autowired	
	public ComponentTypeService(ComponentTypeRepository repo) {
		super(repo,ComponentType.class, String.class);
	}
}
