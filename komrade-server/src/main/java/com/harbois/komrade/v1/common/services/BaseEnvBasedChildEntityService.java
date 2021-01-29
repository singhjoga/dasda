package com.harbois.komrade.v1.common.services;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.harbois.komrade.v1.common.repositories.EnvBasedChildEntityRepository;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;

public abstract class BaseEnvBasedChildEntityService<T extends IdentifiableEntity<ID>, ID extends Serializable, PARENT_ID extends Serializable> extends BaseChildEntityService<T,ID,PARENT_ID>{
	private EnvBasedChildEntityRepository<T, ID,PARENT_ID> repo;	
	public BaseEnvBasedChildEntityService(EnvBasedChildEntityRepository<T, ID, PARENT_ID> repo, Class<T> entityClass, Class<ID> idClass) {
		super(repo,entityClass,idClass);
		this.repo = repo;
	}
	public List<T> findAll(PARENT_ID parentId, Set<String> envCodes) {
		return filter(repo.findAll(parentId, envCodes));
	}
}
