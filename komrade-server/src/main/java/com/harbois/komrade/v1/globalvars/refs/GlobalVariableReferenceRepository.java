package com.harbois.komrade.v1.globalvars.refs;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GlobalVariableReferenceRepository extends JpaRepository<GlobalVariableReference, GlobalVariableReferenceId>{
	List<GlobalVariableReference> findByPropertyId(String propertyId);
	
	String QUERY1="TRUNCATE TABLE GLOBAL_PROP_REF";
	@Modifying
	@Query(value=QUERY1, nativeQuery=true)
	void truncateTable();
}
