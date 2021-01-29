package com.harbois.komrade.v1.common.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;
import com.harbois.komrade.v1.common.repositories.EnvPropertyChildRepository;
import com.harbois.komrade.v1.common.repositories.EnvPropertyValueChildRepository;

public abstract class BaseEnvPropertiesChildService<T extends PersistentPropertyEnvBased<T,V>, V extends PersistentPropertyEnvBasedValue<V>> extends BaseEnvPropertiesService<T, V>{
	private EnvPropertyChildRepository<T> repo;
	private EnvPropertyValueChildRepository<V> valueRepo;
	
	@Autowired
	public BaseEnvPropertiesChildService(EnvPropertyChildRepository<T> repo, Class<T> propClass, EnvPropertyValueChildRepository<V> valueRepo, Class<V> valueClass) {
		super(repo,propClass, valueRepo, valueClass);
		this.repo=repo;
		this.valueRepo=valueRepo;
	}

	public List<T> findAllWithValues(String parentId, Set<String> envCodes) {
		List<T> props = repo.findAll(parentId);
		List<V> values = valueRepo.findByParentIdAndEnvCodeIn(parentId, envCodes);
		setValues(props, values);
		return props;
	}
	public List<T> findAll(String parentId) {
		List<T> props = repo.findAll(parentId);
		return props;
	}
}
