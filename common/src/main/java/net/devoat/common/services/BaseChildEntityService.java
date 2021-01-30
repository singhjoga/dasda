package net.devoat.common.services;

import java.io.Serializable;
import java.util.List;

import net.devoat.common.domain.IdentifiableEntity;
import net.devoat.common.repositories.ChildEntityRepository;

public abstract class BaseChildEntityService<T extends IdentifiableEntity<ID>, ID extends Serializable, PARENT_ID extends Serializable> extends BaseCrudService<T,ID>{
	private ChildEntityRepository<T, ID,PARENT_ID> repo;	
	public BaseChildEntityService(ChildEntityRepository<T, ID, PARENT_ID> repo, Class<T> entityClass, Class<ID> idClass) {
		super(repo,entityClass,idClass);
		this.repo = repo;
	}
	public List<T> findAll(PARENT_ID parentId) {
		return filter(repo.findAll(parentId));
	}
}
