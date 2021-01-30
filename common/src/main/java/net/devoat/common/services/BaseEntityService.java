package net.devoat.common.services;

import java.io.Serializable;
import java.util.List;

import net.devoat.common.domain.IdentifiableEntity;
import net.devoat.common.repositories.EntityRepository;

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
