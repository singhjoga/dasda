package com.harbois.komrade.v1.common.repositories;

import com.harbois.komrade.v1.common.properties.TypedProperty;

public interface EnvPropertyRepository<T extends TypedProperty<T>> extends EntityRepository<T, String>{
	T findByName(String name);
}
