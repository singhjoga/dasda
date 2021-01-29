package com.harbois.oauth.server.tests.funtionaltests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.oauth.server.api.v1.common.RestResponse;
import com.harbois.oauth.server.api.v1.common.RestResponse.AddResult;
import com.harbois.oauth.server.api.v1.groups.Group;
import com.harbois.oauth.server.api.v1.roles.Role;
import com.harbois.oauth.server.api.v1.users.User;
import com.harbois.oauth.server.tests.base.RestException;
import com.harbois.oauth.server.tests.base.TestBase;
import com.harbois.oauth.server.tests.cleints.GroupClient;
import com.harbois.oauth.server.tests.cleints.UserClient;

public class UserTest extends TestBase {

	@Autowired
	private UserClient client;
	@Autowired
	private GroupClient groupClient;
	@Test
	public void testCrud() throws Exception {
		List<User> initial = client.findAll().resultAsList(User.class);

		User obj = client.create("test-group-1", "test-group-1");
		String savedId = client.add(obj).resultAs(AddResult.class).getId();
		User saved = client.get(savedId).resultAs(User.class);;
		compare(obj, saved);
	}
	@Test
	public void attachUserGroupTest() throws Exception{
		User obj = client.create("attachUserGroupTest-1", "attachGroupTest-1");
		String username = client.addAndGetId(obj);
		
		// add groups
		Group grp1 = groupClient.create("attachUserGroupTest-1", "attachGroupTest-1");
		Long groupId1 = groupClient.addAndGetId(grp1);
		Group grp2 = groupClient.create("attachUserGroupTest-2", "attachGroupTest-2");
		Long groupId2 = groupClient.addAndGetId(grp2);
		RestResponse resp = client.attachToGroups(username, groupId1, groupId2);
		Assert.assertFalse(resp.hasErrors());
		List<Group> groups = client.getGroups(username).resultAsList(Group.class);
		Assert.assertEquals(2, groups.size());
		
		//attach one more
		Group grp3 = groupClient.create("attachUserGroupTest-3", "attachGroupTest-3");
		Long groupId3 = groupClient.addAndGetId(grp3);
		resp = client.attachToGroups(username, groupId3, groupId2);
		Assert.assertFalse(resp.hasErrors());
		groups = client.getGroups(username).resultAsList(Group.class);
		Assert.assertEquals(3, groups.size());
		
		//detach one 
		resp = client.detachFromGroups(username, groupId1);
		Assert.assertFalse(resp.hasErrors());
		groups = client.getGroups(username).resultAsList(Group.class);
		Assert.assertEquals(2, groups.size());
		Object found = groups.stream().filter(g ->g.getId().equals(groupId2)).findAny().orElse(null);
		Assert.assertNotNull(found);
		found = groups.stream().filter(g ->g.getId().equals(groupId3)).findAny().orElse(null);
		Assert.assertNotNull(found);
	}	
	@Test
	public void testPassword() throws Exception {
		User obj = client.create("test-group-1", "test-group-pass");
		String savedId = successAdd(client.add(obj));
		User saved = client.get(savedId).resultAs(User.class);
		comparePass(obj.getPassword(), saved.getPassword());

		// saving password with update is not allowed. It should throw an exception
		obj.setPassword("newSecret");
		try {
			success(client.update(savedId, obj));
			Assert.fail("Should throw an exception");
		} catch (RestException e) {
			//It is expected
		}
		success(client.updatePassword(savedId, obj.getPassword()));
		saved = client.get(savedId).resultAs(User.class);
		comparePass(obj.getPassword(), saved.getPassword());	
	}

	private void compare(User expected, User actual) {
		Assert.assertEquals(expected.getName(), actual.getName());
		Assert.assertNotEquals(expected.getPassword(), actual.getPassword());
		Assert.assertEquals(expected.getIsSystem(), actual.getIsSystem());
		
		//TODO: compare secret
	}
}
