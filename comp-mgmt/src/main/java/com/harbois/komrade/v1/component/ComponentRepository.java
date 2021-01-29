package com.harbois.komrade.v1.component;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;

public interface ComponentRepository extends JpaRepository<Component, String>,EntityRepository<Component, String>{

}
