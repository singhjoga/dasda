package com.harbois.komrade.v1.component.version.references;

import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.komrade.v1.common.services.BaseChildEntityService;

@org.springframework.stereotype.Component
public class ComponentVersionReferenceService extends BaseChildEntityService<ComponentVersionReference, String,String>{
	@Autowired	
	public ComponentVersionReferenceService(ComponentVersionReferenceRepository repo) {
		super(repo,ComponentVersionReference.class, String.class);
	}
}
