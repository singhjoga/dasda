package com.harbois.komrade.v1.resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;
import com.harbois.komrade.v1.resources.domain.Host;


public interface HostRepository extends JpaRepository<Host, String>,EntityRepository<Host, String>{
	List<Host> findByResourceCode(String resourceCode);
	//ResourceProperty findByResourceIdAndName(Long resourceId, String name);
}
