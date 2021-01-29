package com.harbois.komrade.v1.common.services;

import java.io.Serializable;
import java.util.List;

import com.harbois.komrade.v1.common.repositories.EntityRepository;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;

public abstract class BaseEntityService<T extends IdentifiableEntity<ID>, ID extends Serializable> extends BaseCrudService<T,ID>{
	private EntityRepository<T, ID> repo;	
	public BaseEntityService(EntityRepository<T, ID> repo, Class<T> entityClass, Class<ID> idClass) {
		super(repo,entityClass,idClass);
		this.repo = repo;
	}
	public List<T> findAll() {
		return filter(repo.findAll());
	}
}
