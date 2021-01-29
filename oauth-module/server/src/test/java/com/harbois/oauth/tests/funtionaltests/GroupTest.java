package com.harbois.oauth.tests.funtionaltests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.common.tests.base.TestBase;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.groups.Group;
import com.harbois.oauth.api.v1.roles.Role;
import com.harbois.oauth.api.v1.users.User;
import com.harbois.oauth.tests.cleints.GroupClient;
import com.harbois.oauth.tests.cleints.RoleClient;
import com.harbois.oauth.tests.cleints.UserClient;

public class GroupTest extends TestBase {

	@Autowired
	private GroupClient client;
	@Autowired
	private RoleClient roleClient;
	@Autowired
	private UserClient userClient;

	@Test
	public void testCrud() throws Exception {
		List<Group> initial = client.findAll().resultAsList(Group.class);

		Group obj = client.create("test-group-1", "test-group-1");
		Long savedId = client.addAndGetId(obj);
		Group saved = client.get(savedId).resultAs(Group.class);
		compare(obj, saved);
	}
	@Test
	public void attachRolesTest() throws Exception{
		Group obj = client.create("attachRolesTest-1", "attachRolesTest-1");
		Long groupId = client.addAndGetId(obj);
		
		// add roles
		Role role1 = roleClient.create("attachRolesTest-1", "attachRolesTest-1");
		Long roleId1 = roleClient.addAndGetId(role1);
		Role role2 = roleClient.create("attachRolesTest-2", "attachRolesTest-2");
		Long roleId2 = roleClient.addAndGetId(role2);
		RestResponse resp = client.attachRoles(groupId, roleId1, roleId2);
		Assert.assertFalse(resp.hasErrors());
		List<Role> roles = client.getRoles(groupId).resultAsList(Role.class);
		Assert.assertEquals(2, roles.size());
		
		//attach one more
		Role role3 = roleClient.create("attachRolesTest-3", "attachRolesTest-3");
		Long roleId3 = roleClient.addAndGetId(role3);
		resp = client.attachRoles(groupId, roleId3, roleId2);
		Assert.assertFalse(resp.hasErrors());
		roles = client.getRoles(groupId).resultAsList(Role.class);
		Assert.assertEquals(3, roles.size());
		
		//detach one 
		resp = client.detachRoles(groupId, roleId1);
		Assert.assertFalse(resp.hasErrors());
		roles = client.getRoles(groupId).resultAsList(Role.class);
		Assert.assertEquals(2, roles.size());
		Object found = roles.stream().filter(g ->g.getId().equals(roleId2)).findAny().orElse(null);
		Assert.assertNotNull(found);
		found = roles.stream().filter(g ->g.getId().equals(roleId3)).findAny().orElse(null);
		Assert.assertNotNull(found);
	}

	@Test
	public void attachUsersTest() throws Exception{
		Group obj = client.create("attachUsersTest-1", "attachRolesTest-1");
		Long groupId = client.addAndGetId(obj);
		
		// add roles
		User user1 = userClient.create("attachUsersTest-1", "attachRolesTest-1");
		String userId1 = userClient.addAndGetId(user1);
		User user2 = userClient.create("attachUsersTest-2", "attachRolesTest-2");
		String userId2 = userClient.addAndGetId(user2);
		RestResponse resp = client.attachUsers(groupId, userId1, userId2);
		Assert.assertFalse(resp.hasErrors());
		List<User> users = client.getUsers(groupId).resultAsList(User.class);
		Assert.assertEquals(2, users.size());
		
		//attach one more
		User user3 = userClient.create("attachUsersTest-3", "attachRolesTest-3");
		String userId3 = userClient.addAndGetId(user3);
		resp = client.attachUsers(groupId, userId3, userId2);
		Assert.assertFalse(resp.hasErrors());
		users = client.getUsers(groupId).resultAsList(User.class);
		Assert.assertEquals(3, users.size());
		
		//detach one 
		resp = client.detachUsers(groupId, userId1);
		Assert.assertFalse(resp.hasErrors());
		users = client.getUsers(groupId).resultAsList(User.class);
		Assert.assertEquals(2, users.size());
		Object found = users.stream().filter(g ->g.getId().equals(userId2)).findAny().orElse(null);
		Assert.assertNotNull(found);
		found = users.stream().filter(g ->g.getId().equals(userId3)).findAny().orElse(null);
		Assert.assertNotNull(found);
	}
	
	private void compare(Group expected, Group actual) {
		Assert.assertEquals(expected.getName(), actual.getName());
		Assert.assertEquals(expected.getDescription(), actual.getDescription());
		Assert.assertEquals(expected.getIsSystem(), actual.getIsSystem());
		
		//TODO: compare secret
	}
}
