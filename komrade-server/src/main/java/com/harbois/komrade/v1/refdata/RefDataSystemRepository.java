package com.harbois.komrade.v1.refdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;


public interface RefDataSystemRepository extends JpaRepository<RefDataSystem, String>,EntityRepository<RefDataSystem, String>{
	public List<RefDataSystem> findByReferenceType(String referenceType);
}
