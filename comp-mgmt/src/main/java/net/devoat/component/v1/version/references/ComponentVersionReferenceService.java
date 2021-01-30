package net.devoat.component.v1.version.references;

import org.springframework.beans.factory.annotation.Autowired;

import net.devoat.common.services.BaseChildEntityService;

@org.springframework.stereotype.Component
public class ComponentVersionReferenceService extends BaseChildEntityService<ComponentVersionReference, String,String>{
	@Autowired	
	public ComponentVersionReferenceService(ComponentVersionReferenceRepository repo) {
		super(repo,ComponentVersionReference.class, String.class);
	}
}
