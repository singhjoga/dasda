package com.harbois.komrade.v1.system.external;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;

public interface ExternalSystemRepository extends JpaRepository<ExternalSystem, String>,EntityRepository<ExternalSystem, String>{
	List<ExternalSystem> findBySystemTypeCode(String systemTypeCode);
}
