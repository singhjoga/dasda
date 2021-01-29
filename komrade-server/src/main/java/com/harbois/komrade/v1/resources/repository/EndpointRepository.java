package com.harbois.komrade.v1.resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;
import com.harbois.komrade.v1.resources.domain.Endpoint;


public interface EndpointRepository extends JpaRepository<Endpoint, String>,EntityRepository<Endpoint, String>{
	List<Endpoint> findByResourceCode(String resourceCode);
}
