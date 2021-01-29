package com.harbois.oauth.server.api.v1.roles;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long>{
	String QUERY1 = "SELECT * FROM USER_ROLE WHERE USER_ROLE_ID IN ("+
                     "SELECT USER_ROLE_ID FROM USER_GROUP_ROLE WHERE USER_GROUP_ID=?1)";
	@Query(value=QUERY1, nativeQuery=true)
	List<Role> findByGroupId(Long groupId);
	
	String QUERY2 = "SELECT * FROM USER_ROLE WHERE USER_ROLE_ID IN ("+
            "SELECT DISTINCT USER_ROLE_ID FROM USER_GROUP_ROLE WHERE USER_GROUP_ID IN ("+
			"SELECT DISTINCT USER_GROUP_ID FROM USER_GROUP_USER WHERE USERNAME=?1)) AND "+
            "(CLIENT_ID=?2 OR CLIENT_ID='global')";
	@Query(value=QUERY2, nativeQuery=true)
	List<Role> findByUsername(String username, String clientId);
	
	String QUERY3 = "SELECT * FROM USER_ROLE WHERE USER_ROLE_ID IN ("+
            "SELECT DISTINCT USER_ROLE_ID FROM USER_GROUP_ROLE WHERE USER_GROUP_ID IN ("+
			"SELECT DISTINCT USER_GROUP_ID FROM USER_GROUP_USER WHERE USERNAME=?1)) AND "+
            "(CLIENT_ID=?2 OR CLIENT_ID='global') AND IS_SYSTEM=1";
	@Query(value=QUERY3, nativeQuery=true)
	List<Role> findSystemRoles(String username, String clientId);
}
