package com.harbois.oauth.server.tests.funtionaltests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.oauth.server.tests.base.RestException;
import com.harbois.oauth.server.tests.base.TestBase;
import com.harbois.oauth.server.tests.cleints.ClientsClient;
import com.harbois.oauth.server.tests.cleints.TestData;

public class BasicSecurityTest extends TestBase {

	@Autowired
	private ClientsClient client;
	@Autowired
	private TestData testData;
	@Test
	public void testEndpointAccess() throws Exception{
		//skip oauth authentication first
		client.setSkipAuthentication(true);
		try {
			client.findAll();
			Assert.fail("Un-authenticated user should not be allowed to access the API");
		}catch(RestException e) {
			Assert.assertTrue(e.getMessage().contains("unauthorized"));
		}
		client.setSkipAuthentication(false);
		//SysAdmin, ClientAdmin, UserAdmins and Readers can access the API
		testData.init();
		client.setUsername(TestData.USERNAME_SYSADMIN);
		client.setPassword(TestData.USER_PASSWORD);
		client.findAll();

		client.setUsername(TestData.USERNAME_CLIENT_ADMIN);
		client.setPassword(TestData.USER_PASSWORD);
		client.findAll();
		
		client.setUsername(TestData.USERNAME_USERADMIN);
		client.setPassword(TestData.USER_PASSWORD);
		client.findAll();

		client.setUsername(TestData.USERNAME_READER);
		client.setPassword(TestData.USER_PASSWORD);
		client.findAll();
		
		client.setUsername(TestData.USERNAME_NORMAL);
		client.setPassword(TestData.USER_PASSWORD);

		try {
			client.findAll();
			Assert.fail("Normal user should not be allowed to access API");
		}catch(RestException e) {
			Assert.assertTrue(e.getMessage().contains("access_denied"));
		}

	}

}
