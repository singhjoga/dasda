package net.devoat.component.v1.type;

import org.springframework.data.jpa.repository.JpaRepository;

import net.devoat.common.repositories.EntityRepository;

public interface ComponentTypeRepository extends JpaRepository<ComponentType, String>,EntityRepository<ComponentType, String>{

}
