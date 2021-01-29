package com.harbois.komrade.auth;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import com.harbois.komrade.auth.parsing.ParseException;
import com.thetechnovator.common.java.StringProperties;


public class AuthManageTest {
/*
	@Test
	public void testEntityActionParser() throws IOException, ParseException {
		AuthorizationManager.init();
		Set<String> roles = new TreeSet<>();
		roles.add("sysadmin");
		AuthorizationManager.refreshUserPermissions("test", roles);
		Assert.assertTrue(AuthorizationManager.hasPermission("test", "group", "add", null));
		Assert.assertFalse(AuthorizationManager.hasPermission("test", "group", "xxxx", null));
		Assert.assertFalse(AuthorizationManager.hasPermission("test", "component", "add", null));
		Assert.assertFalse(AuthorizationManager.hasPermission("test", "component", "deploy", null));
		roles.add("dev-team");
		AuthorizationManager.refreshUserPermissions("test", roles);
		//dev-team role gives permission to component
		Assert.assertTrue(AuthorizationManager.hasPermission("test", "component", "add", null));
		Assert.assertTrue(AuthorizationManager.hasPermission("test", "component", "delete", null));
		roles.clear();
		roles.add("dev-team");
		AuthorizationManager.refreshUserPermissions("test", roles);
		//now the user has only dev-team role, which allows component
		Assert.assertTrue(AuthorizationManager.hasPermission("test", "component", "add", null));
		Assert.assertTrue(AuthorizationManager.hasPermission("test", "component", "delete", null));
		StringProperties vars = new StringProperties();
		
		//no vars, should fail
		try {
			Assert.assertFalse(AuthorizationManager.hasPermission("test", "component", "deploy", vars));
			Assert.fail("Show throw an exception when the required constraint variables are not provided");
		} catch (IllegalArgumentException e) {
			//expected
		}
		vars.put("environment", "PRD");
		//dev-team cannot deploy in PRD, should fail
		Assert.assertFalse(AuthorizationManager.hasPermission("test", "component", "deploy", vars));
		vars.put("environment", "DEV");
		//should pass
		Assert.assertTrue(AuthorizationManager.hasPermission("test", "component", "deploy", vars));
		vars.put("environment", "DEV-A");
		//should pass, constrain is wildcard based i.e. DEV*
		Assert.assertTrue(AuthorizationManager.hasPermission("test", "component", "deploy", vars));
	}
	*/
}
