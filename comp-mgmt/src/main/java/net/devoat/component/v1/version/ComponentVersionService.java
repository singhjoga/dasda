package net.devoat.component.v1.version;

import org.springframework.beans.factory.annotation.Autowired;

import net.devoat.common.services.BaseChildEntityService;

@org.springframework.stereotype.Component
public class ComponentVersionService extends BaseChildEntityService<ComponentVersion, String,String>{
	@Autowired	
	public ComponentVersionService(ComponentVersionRepository repo) {
		super(repo,ComponentVersion.class, String.class);
	}
}
