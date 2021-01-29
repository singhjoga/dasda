package com.harbois.komrade.v1.common.repositories;

import java.util.List;

import com.harbois.komrade.v1.common.properties.TypedProperty;

public interface EnvPropertyChildRepository<T extends TypedProperty<T>> extends EnvPropertyRepository<T>{
	T findByName(String parentId, String name);
	List<T> findAll(String parentId);
}

