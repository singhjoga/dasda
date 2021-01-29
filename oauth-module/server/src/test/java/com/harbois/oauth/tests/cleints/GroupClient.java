package com.harbois.oauth.tests.cleints;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harbois.common.tests.base.IdentifiableClient;
import com.harbois.common.tests.base.RestException;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.groups.Group;
import com.harbois.oauth.api.v1.roles.Role;
import com.harbois.oauth.api.v1.users.User;
@Service
public class GroupClient extends IdentifiableClient<Group, Long>{
	public static final String URL="/api/v1/groups";
	public GroupClient() {
		super(URL, Group.class, Long.class);
	}
	public Group create(String name, String description) {
		return create(name,description,"global");
	}
	public Group create(String name, String description, String clientId) {
		Group obj = new Group();
		obj.setDescription(description);
		obj.setName(name);
		obj.setIsSystem(false);
		obj.setClientId(clientId);
		return obj;
	}

	public RestResponse attachRoles(Long groupId, Long... roleIds) throws RestException{
		String url = getUrl(groupId)+"/attachroles";
		return updateResource(url, roleIds);
	}
	public RestResponse detachRoles(Long groupId, Long... roleIds) throws RestException{
		String url = getUrl(groupId)+"/detachroles";
		return updateResource(url, roleIds);
	}
	public RestResponse getRoles(Long groupId) throws RestException{
		String url = getUrl(groupId)+"/roles";
		return getResourceAsList(url, Role.class);
	}
	public RestResponse attachUsers(Long groupId, String... usernames) throws RestException{
		String url = getUrl(groupId)+"/attachusers";
		return updateResource(url, usernames);
	}
	public RestResponse detachUsers(Long groupId, String... usernames) throws RestException{
		String url = getUrl(groupId)+"/detachusers";
		return updateResource(url, usernames);
	}
	public RestResponse getUsers(Long groupId) throws RestException{
		String url = getUrl(groupId)+"/users";
		return getResourceAsList(url, User.class);
	}
	@Override
	public Group createTestObject(String clientId) {
		return create(UUID.randomUUID().toString(),"Test description",clientId);
	}
	
}
