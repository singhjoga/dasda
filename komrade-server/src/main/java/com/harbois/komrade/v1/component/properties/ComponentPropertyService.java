package com.harbois.komrade.v1.component.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEnvPropertiesChildService;

@Component
public class ComponentPropertyService extends BaseEnvPropertiesChildService<ComponentProperty, ComponentPropertyValue>{
	
	@Autowired
	public ComponentPropertyService(ComponentPropertyRepository repo, ComponentPropertyValueRepository valueRepo) {
		super(repo,ComponentProperty.class, valueRepo, ComponentPropertyValue.class);
	}
}
