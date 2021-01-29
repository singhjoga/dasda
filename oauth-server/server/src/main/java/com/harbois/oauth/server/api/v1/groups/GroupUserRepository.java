package com.harbois.oauth.server.api.v1.groups;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.harbois.oauth.server.api.v1.groups.GroupUser.GroupUserId;

public interface GroupUserRepository extends CrudRepository<GroupUser, GroupUserId>{
	String QUERY1 = "SELECT * FROM USER_GROUP_USER WHERE USER_GROUP_ID=?1";
	@Query(value=QUERY1, nativeQuery=true)
	List<GroupUser> findByGroupId(Long groupId);
	String QUERY2 = "SELECT * FROM USER_GROUP_USER WHERE USERNAME=?1";
	@Query(value=QUERY2, nativeQuery=true)
	List<GroupUser> findByUsername(String username);
}
