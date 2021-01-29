package com.harbois.komrade.v1.globalvars;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbois.komrade.v1.common.repositories.EnvPropertyValueRepository;

public interface GlobalVariableValueRepository extends EnvPropertyValueRepository<GlobalVariableValue>, JpaRepository<GlobalVariableValue, String>{
	String QUERY1 = "SELECT * FROM GLOBAL_VAR_VAL  WHERE ENV_CODE=?1 AND GLOBAL_VAR_ID = ?2";
	@Query(value=QUERY1, nativeQuery=true)
	GlobalVariableValue findByEnvCodeAndPropertyId(String envCode, String globalPropertyId);
	
	String QUERY2 = "SELECT * FROM GLOBAL_VAR_VAL  WHERE ENV_CODE=?1 AND GLOBAL_VAR_ID IN ?2";
	@Query(value=QUERY2, nativeQuery=true)
	List<GlobalVariableValue> findByEnvCodeAndPropertyIdIn(String envCode, List<String> globalPropertyIds);

	String QUERY22 = "SELECT * FROM GLOBAL_VAR_VAL  WHERE ENV_CODE IN ?1 AND GLOBAL_VAR_ID IN ?2";
	@Query(value=QUERY22, nativeQuery=true)
	List<GlobalVariableValue> findByEnvCodeInAndPropertyIdIn(Set<String> envCode, List<String> globalPropertyIds);
	
	String QUERY3 = "SELECT * FROM GLOBAL_VAR_VAL  WHERE ENV_CODE=?1";
	@Query(value=QUERY3, nativeQuery=true)
	List<GlobalVariableValue> findByEnvCode(String envCode);
	
	//String QUERY4 = "SELECT * FROM GLOBAL_VAR_VAL  WHERE ENV_CODE IN?1";
	//@Query(value=QUERY4, nativeQuery=true)
	List<GlobalVariableValue> findByEnvCodeIn(Set<String> envCodes);

	List<GlobalVariableValue> findByPropertyId(String proprId);

}
