package com.harbois.komrade.v1.system.properties;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;

@Component
public class SystemPropertyService extends BaseEntityService<SystemProperty, String>{
	private SystemPropertyRepository repo;
	@Autowired	
	public SystemPropertyService(SystemPropertyRepository repo) {
		super(repo,SystemProperty.class, String.class);
		this.repo=repo;
	}

	@Override
	public SystemProperty add(SystemProperty obj) {
		throw new BadRequestException("Operation not allowed");
	}
	
	public List<SystemProperty> findByCategory(String category) {
		return repo.findByCategory(category);
	}
	
}
