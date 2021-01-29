package com.harbois.oauth.api.v1.users;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>{
	String QUERY1 = "SELECT * FROM USER WHERE USERNAME IN ("+
            "SELECT USERNAME FROM USER_GROUP_USER WHERE USER_GROUP_ID=?1)";
	@Query(value=QUERY1, nativeQuery=true)
	List<User> findByGroupId(Long groupId);
}
