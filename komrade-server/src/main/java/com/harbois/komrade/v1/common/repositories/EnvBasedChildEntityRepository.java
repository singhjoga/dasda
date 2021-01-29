package com.harbois.komrade.v1.common.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface EnvBasedChildEntityRepository<T, ID extends Serializable,PARENT_ID extends Serializable> extends ChildEntityRepository<T, ID, PARENT_ID>{
	List<T> findAll(PARENT_ID parentId, Set<String> envCodes);
}
