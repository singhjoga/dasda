package com.harbois.komrade.v1.component.version.properties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbois.komrade.v1.common.repositories.EnvPropertyChildRepository;

public interface ComponentVersionPropertyRepository extends EnvPropertyChildRepository<ComponentVersionProperty>, JpaRepository<ComponentVersionProperty, String>{
	String QUERY1 = "SELECT * FROM COMP_VER_PROP WHERE COMP_VER_ID=?1 AND NAME=?2";
	@Query(value=QUERY1, nativeQuery=true)
	ComponentVersionProperty findByName(String componentVersionId, String name);

	String QUERY2 = "SELECT * FROM COMP_VER_PROP WHERE COMP_VER_ID=?1";
	@Query(value=QUERY2, nativeQuery=true)
	List<ComponentVersionProperty> findAll(String componentVersionId);
}
