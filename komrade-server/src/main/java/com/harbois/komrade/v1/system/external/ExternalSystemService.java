package com.harbois.komrade.v1.system.external;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;

@Component
public class ExternalSystemService extends BaseEntityService<ExternalSystem, String>{
	private ExternalSystemRepository repo;
	@Autowired	
	public ExternalSystemService(ExternalSystemRepository repo) {
		super(repo,ExternalSystem.class, String.class);
		this.repo=repo;
	}


	public List<ExternalSystem> findBySystenType(String systemTypeCode) {
		return repo.findBySystemTypeCode(systemTypeCode);
	}
	
}
