package com.harbois.komrade.v1.common.repositories;

import java.util.List;
import java.util.Set;

import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;

public interface EnvPropertyValueRepository<T extends PersistentPropertyEnvBasedValue<T>> extends EntityRepository<T, String>{

	List<T> findByPropertyId(String id);

	List<T> findByEnvCodeIn(Set<String> envCodes);

	T findByEnvCodeAndPropertyId(String envCode, String id);
}
