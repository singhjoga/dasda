package com.harbois.komrade.v1.component;

import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.komrade.v1.common.services.BaseEntityService;

@org.springframework.stereotype.Component
public class ComponentService extends BaseEntityService<Component, String>{
	@Autowired	
	public ComponentService(ComponentRepository repo) {
		super(repo,Component.class, String.class);
	}
}
