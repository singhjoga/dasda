package net.devoat.component.v1;

import org.springframework.data.jpa.repository.JpaRepository;

import net.devoat.common.repositories.EntityRepository;

public interface ComponentRepository extends JpaRepository<Component, String>,EntityRepository<Component, String>{

}
