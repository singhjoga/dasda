package com.harbois.oauth.tests.cleints;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harbois.common.tests.base.IdentifiableClient;
import com.harbois.common.tests.base.RestException;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.controllers.dtos.Password;
import com.harbois.oauth.api.v1.groups.Group;
import com.harbois.oauth.api.v1.users.User;
@Service
public class UserClient extends IdentifiableClient<User, String>{
	public static final String URL="/api/v1/users";
	public UserClient() {
		super(URL, User.class, String.class);
	}
	
	public User create(String name, String pass) {
		User obj = new User();
		obj.setName(name);
		obj.setIsSystem(false);
		obj.setPassword(pass);
		return obj;
	}
	public RestResponse updatePassword(String id, String newPassword) throws RestException{
		String url=getUrl(id)+"/updatepassword";
		Password obj = new Password(newPassword);
		return updateResource(url, obj);
	}
	public RestResponse attachToGroups(String username, Long... groupIds) throws RestException{
		String url = getUrl(username)+"/attachtogroups";
		return updateResource(url, groupIds);
	}
	public RestResponse detachFromGroups(String username, Long... groupIds) throws RestException{
		String url = getUrl(username)+"/detachfromgroups";
		return updateResource(url, groupIds);
	}
	public RestResponse getGroups(String username) throws RestException{
		String url = getUrl(username)+"/groups";
		return getResourceAsList(url, Group.class);
	}

	@Override
	public User createTestObject(String clientId) {
		return create(UUID.randomUUID().toString(),"test123");
	}
		
}
