package com.harbois.komrade.v1.common.repositories;

import java.io.Serializable;
import java.util.List;

public interface EntityRepository<T, ID extends Serializable> extends BaseRepository<T, ID>{
	List<T> findAll();
}
