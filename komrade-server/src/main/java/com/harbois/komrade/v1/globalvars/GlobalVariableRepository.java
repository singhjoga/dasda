package com.harbois.komrade.v1.globalvars;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbois.komrade.v1.common.repositories.EnvPropertyRepository;

public interface GlobalVariableRepository extends EnvPropertyRepository<GlobalVariable>, JpaRepository<GlobalVariable, String>{
	GlobalVariable findByName(String name);
	
	String QUERY4 = "SELECT * FROM GLOBAL_VAR WHERE IS_DISABLED=?1 ORDER BY NAME";
	@Query(value=QUERY4, nativeQuery=true)
	List<GlobalVariable> findAll(int disabled);	

	String QUERY2 = "SELECT * FROM GLOBAL_VAR WHERE IS_DISABLED=0 AND name IN ?1  ORDER BY NAME";
	@Query(value=QUERY2, nativeQuery=true)
	List<GlobalVariable> findByNameInNotDeleted(List<String> names);

	String QUERY3 = "SELECT * FROM GLOBAL_VAR WHERE IS_DISABLED=0 AND GLOBAL_VAR_ID IN ?1  ORDER BY NAME";
	@Query(value=QUERY2, nativeQuery=true)
	List<GlobalVariable> findByIdInNotDeleted(List<Long> ids);

	String QUERY5 = "SELECT * FROM GLOBAL_VAR WHERE IS_DISABLED=?1 AND GLOBAL_VAR_ID IN (SELECT GLOBAL_VAR_ID FROM GLOBAL_VAR_VAL WHERE env_Code IN ?2)  ORDER BY NAME";
	@Query(value=QUERY5, nativeQuery=true)
	List<GlobalVariable> findByEnvCodeIn(Boolean deleted, Set<String> envCodes);
	
	String EXISTS_VALUES="SELECT CASE WHEN cnt=0 THEN 'FALSE' ELSE 'TRUE' END FROM (SELECT COUNT(*) as cnt FROM GLOBAL_VAR WHERE GLOBAL_VAR_id=?1) a";
	@Query(value=EXISTS_VALUES, nativeQuery=true)
	public Boolean existsValues(Long id);
}
