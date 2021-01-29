package com.harbois.komrade.v1.component.version;

import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.komrade.v1.common.services.BaseChildEntityService;

@org.springframework.stereotype.Component
public class ComponentVersionService extends BaseChildEntityService<ComponentVersion, String,String>{
	@Autowired	
	public ComponentVersionService(ComponentVersionRepository repo) {
		super(repo,ComponentVersion.class, String.class);
	}
}
