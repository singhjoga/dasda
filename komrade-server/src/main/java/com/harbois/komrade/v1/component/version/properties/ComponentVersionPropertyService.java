package com.harbois.komrade.v1.component.version.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEnvPropertiesChildService;

@Component
public class ComponentVersionPropertyService extends BaseEnvPropertiesChildService<ComponentVersionProperty, ComponentVersionPropertyValue>{
	
	@Autowired
	public ComponentVersionPropertyService(ComponentVersionPropertyRepository repo, ComponentVersionPropertyValueRepository valueRepo) {
		super(repo,ComponentVersionProperty.class, valueRepo, ComponentVersionPropertyValue.class);
	}
}
