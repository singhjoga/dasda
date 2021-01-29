package com.harbois.oauth.tests.funtionaltests;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.common.tests.base.TestBase;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.groups.Group;
import com.harbois.oauth.api.v1.roles.Role;
import com.harbois.oauth.tests.cleints.GroupClient;
import com.harbois.oauth.tests.cleints.RoleClient;

public class RoleTest extends TestBase {

	@Autowired
	private RoleClient client;
	@Autowired
	private GroupClient groupClient;

	@Test
	public void testCrud() throws Exception {
		List<Role> initial = client.findAll().resultAsList(Role.class);

		Role obj = client.create("test-group-1", "test-group-1");
		Long savedId = client.addAndGetId(obj);
		Role saved = client.get(savedId).resultAs(Role.class);;
		compare(obj, saved);
	}
	@Test
	public void attachGroupTest() throws Exception{
		Role obj = client.create("attachGroupTest-1", "attachGroupTest-1");
		Long roleId = client.addAndGetId(obj);
		
		// add groups
		Group grp1 = groupClient.create("attachGroupTest-1", "attachGroupTest-1");
		Long groupId1 = groupClient.addAndGetId(grp1);
		Group grp2 = groupClient.create("attachGroupTest-2", "attachGroupTest-2");
		Long groupId2 = groupClient.addAndGetId(grp2);
		RestResponse resp = client.attachToGroups(roleId, groupId1, groupId2);
		Assert.assertFalse(resp.hasErrors());
		List<Group> groups = client.getGroups(roleId).resultAsList(Group.class);
		Assert.assertEquals(2, groups.size());
		
		//attach one more
		Group grp3 = groupClient.create("attachGroupTest-3", "attachGroupTest-3");
		Long groupId3 = groupClient.addAndGetId(grp3);
		resp = client.attachToGroups(roleId, groupId3, groupId2);
		Assert.assertFalse(resp.hasErrors());
		groups = client.getGroups(roleId).resultAsList(Group.class);
		Assert.assertEquals(3, groups.size());
		
		//detach one 
		resp = client.detachFromGroups(roleId, groupId1);
		Assert.assertFalse(resp.hasErrors());
		groups = client.getGroups(roleId).resultAsList(Group.class);
		Assert.assertEquals(2, groups.size());
		Object found = groups.stream().filter(g ->g.getId().equals(groupId2)).findAny().orElse(null);
		Assert.assertNotNull(found);
		found = groups.stream().filter(g ->g.getId().equals(groupId3)).findAny().orElse(null);
		Assert.assertNotNull(found);
	}
	private void compare(Role expected, Role actual) {
		Assert.assertEquals(expected.getName(), actual.getName());
		Assert.assertEquals(expected.getDescription(), actual.getDescription());
		Assert.assertEquals(expected.getIsSystem(), actual.getIsSystem());
		
		//TODO: compare secret
	}
}
