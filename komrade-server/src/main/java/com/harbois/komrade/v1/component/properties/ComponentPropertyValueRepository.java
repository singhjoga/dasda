package com.harbois.komrade.v1.component.properties;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbois.komrade.v1.common.repositories.EnvPropertyValueChildRepository;

public interface ComponentPropertyValueRepository extends EnvPropertyValueChildRepository<ComponentPropertyValue>, JpaRepository<ComponentPropertyValue, String>{
	String QUERY1 = "SELECT * FROM COMP_PROP_VAL WHERE ENV_CODE IN ?2 AND COMP_PROP_ID IN (SELECT COMP_PROP_ID FROM COMP_PROP WHERE COMP_ID=?1)";
	@Query(value=QUERY1, nativeQuery=true)
	List<ComponentPropertyValue> findByParentIdAndEnvCodeIn(String componentId, Set<String> envCodes);
}
