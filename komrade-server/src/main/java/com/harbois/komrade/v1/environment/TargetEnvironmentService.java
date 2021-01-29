package com.harbois.komrade.v1.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;

@Component
public class TargetEnvironmentService extends BaseEntityService<TargetEnvironment, String>{
	@Autowired	
	public TargetEnvironmentService(TargetEnvironmentRepository repo) {
		super(repo,TargetEnvironment.class, String.class);
	}
}
