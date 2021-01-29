package com.harbois.komrade.v1.component.version.properties;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbois.komrade.v1.common.repositories.EnvPropertyValueChildRepository;

public interface ComponentVersionPropertyValueRepository extends EnvPropertyValueChildRepository<ComponentVersionPropertyValue>, JpaRepository<ComponentVersionPropertyValue, String>{
	String QUERY1 = "SELECT * FROM COMP_VER_PROP_VAL WHERE ENV_CODE IN ?2 AND COMP_VER_PROP_ID IN (SELECT COMP_VER_PROP_ID FROM COMP_VER_PROP WHERE COMP_VER_ID=?1)";
	@Query(value=QUERY1, nativeQuery=true)
	List<ComponentVersionPropertyValue> findByParentIdAndEnvCodeIn(String componentVersionId, Set<String> envCodes);
}
