package com.harbois.oauth.api.v1.groups;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long>{
	String QUERY1 = "SELECT * FROM USER_GROUP WHERE USER_GROUP_ID IN ("+
            "SELECT USER_GROUP_ID FROM USER_GROUP_ROLE WHERE USER_ROLE_ID=?1)";
	@Query(value=QUERY1, nativeQuery=true)
	List<Group> findByRoleId(Long roleId);
	
	String QUERY2 = "SELECT * FROM USER_GROUP WHERE USER_GROUP_ID IN ("+
            "SELECT USER_GROUP_ID FROM USER_GROUP_USER WHERE USERNAME=?1) AND (CLIENT_ID=?2 OR CLIENT_ID='global')";
	@Query(value=QUERY2, nativeQuery=true)
	List<Group> findByUsername(String username, String clientId);	
}
