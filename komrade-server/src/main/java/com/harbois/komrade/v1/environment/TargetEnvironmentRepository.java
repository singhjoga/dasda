package com.harbois.komrade.v1.environment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;

public interface TargetEnvironmentRepository extends JpaRepository<TargetEnvironment, String>,EntityRepository<TargetEnvironment, String>{

}
