package net.devoat.component.v1.version.actions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.devoat.common.repositories.ChildEntityRepository;

public interface ComponentVersionActionRepository extends JpaRepository<ComponentVersionAction, String>,ChildEntityRepository<ComponentVersionAction, String, String>{
	String QUERY2 = "SELECT * FROM COMP_VER_ACT WHERE COMP_VER_ID=?1";
	@Query(value=QUERY2, nativeQuery=true)
	List<ComponentVersionAction> findAll(String componentVersionId);
}
