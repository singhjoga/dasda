package com.harbois.komrade.v1.component.type;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbois.komrade.v1.common.repositories.EntityRepository;

public interface ComponentTypeRepository extends JpaRepository<ComponentType, String>,EntityRepository<ComponentType, String>{

}
