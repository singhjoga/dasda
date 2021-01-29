package com.harbois.komrade.v1.globalvars.refs;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalVariableReferenceViewRepository extends JpaRepository<GlobalVariableReferenceView, GlobalVariableReferenceId>{
	List<GlobalVariableReferenceView> findByPropertyId(String propertyId); 
}
