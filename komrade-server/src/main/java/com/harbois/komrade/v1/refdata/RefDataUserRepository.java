package com.harbois.komrade.v1.refdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;


public interface RefDataUserRepository extends JpaRepository<RefDataUser, String>,EntityRepository<RefDataUser, String>{
	public List<RefDataUser> findByReferenceType(String referenceType);
}
