package com.harbois.komrade.v1.system.properties;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;

public interface SystemPropertyRepository extends JpaRepository<SystemProperty, String>,EntityRepository<SystemProperty, String>{
	List<SystemProperty> findByCategory(String category);
}
