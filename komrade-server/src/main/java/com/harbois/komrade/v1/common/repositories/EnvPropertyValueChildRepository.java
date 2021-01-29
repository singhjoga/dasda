package com.harbois.komrade.v1.common.repositories;

import java.util.List;
import java.util.Set;

import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;

public interface EnvPropertyValueChildRepository<T extends PersistentPropertyEnvBasedValue<T>> extends EnvPropertyValueRepository<T>{
	List<T> findByParentIdAndEnvCodeIn(String parentId, Set<String> envCodes);
}
