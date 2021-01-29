package com.harbois.oauth.server.tests.cleints;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harbois.oauth.server.api.v1.common.RestResponse;
import com.harbois.oauth.server.api.v1.groups.Group;
import com.harbois.oauth.server.api.v1.roles.Role;
import com.harbois.oauth.server.tests.base.IdentifiableClient;
import com.harbois.oauth.server.tests.base.RestException;
@Service
public class RoleClient extends IdentifiableClient<Role, Long>{
	public static final String URL="/api/v1/roles";
	public RoleClient() {
		super(URL, Role.class, Long.class);
	}
	public Role create(String name, String description) {
		return create(name,description,"global");
	}
	public Role create(String name, String description, String clientId) {
		Role obj = new Role();
		obj.setDescription(description);
		obj.setName(name);
		obj.setIsSystem(false);
		obj.setClientId(clientId);
		return obj;
	}
	
	public RestResponse attachToGroups(Long roleId, Long... groupIds) throws RestException{
		String url = getUrl(roleId)+"/attachtogroups";
		return updateResource(url, groupIds);
	}
	public RestResponse detachFromGroups(Long roleId, Long... groupIds) throws RestException{
		String url = getUrl(roleId)+"/detachfromgroups";
		return updateResource(url, groupIds);
	}
	public RestResponse getGroups(Long roleId) throws RestException{
		String url = getUrl(roleId)+"/groups";
		return getResourceAsList(url, Group.class);
	}
	@Override
	public Role createTestObject(String clientId) {
		return create(UUID.randomUUID().toString(),"Test description",clientId);
	}
	
}
