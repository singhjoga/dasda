package com.harbois.komrade.v1.common.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID extends Serializable> {

	Optional<T> findById(ID id);

	T save(T obj);

	void delete(T saved);

	List<T> findAll();

}
