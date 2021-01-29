package com.harbois.oauth.server.api.v1.groups;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.harbois.oauth.server.api.v1.groups.GroupRole.GroupRoleId;

public interface GroupRoleRepository extends CrudRepository<GroupRole, GroupRoleId>{
	String QUERY1 = "SELECT * FROM USER_GROUP_ROLE WHERE USER_GROUP_ID=?1";
	@Query(value=QUERY1, nativeQuery=true)
	List<GroupRole> findByGroupId(Long groupId);

	String QUERY2 = "SELECT * FROM USER_GROUP_ROLE WHERE USER_ROLE_ID=?1";
	@Query(value=QUERY2, nativeQuery=true)
	List<GroupRole> findByRoleId(Long roleId);
	
}
