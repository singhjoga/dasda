package net.devoat.component.v1.version;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.devoat.common.repositories.ChildEntityRepository;

public interface ComponentVersionRepository extends JpaRepository<ComponentVersion, String>,ChildEntityRepository<ComponentVersion, String, String>{
	String QUERY2 = "SELECT * FROM COMP_VER WHERE COMP_ID=?1";
	@Query(value=QUERY2, nativeQuery=true)
	List<ComponentVersion> findAll(String componentId);
}
