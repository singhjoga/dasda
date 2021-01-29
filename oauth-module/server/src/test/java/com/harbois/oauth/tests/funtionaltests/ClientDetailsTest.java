package com.harbois.oauth.tests.funtionaltests;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.common.tests.base.IdentifiableClient;
import com.harbois.common.tests.base.RestException;
import com.harbois.oauth.api.v1.clients.OAuthClient;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.ValidationErrorDetail;
import com.harbois.oauth.api.v1.common.exception.ErrorCodes;
import com.harbois.oauth.tests.cleints.ClientsClient;
import com.harbois.oauth.tests.cleints.TestData;

public class ClientDetailsTest extends BaseIdentifiableTest<OAuthClient, String> {

	@Autowired
	private ClientsClient client;
	@Autowired
	private TestData testData;
	
	public ClientDetailsTest() {
		super(OAuthClient.class, ClientsClient.class);
	}

	@Before
	public void init() throws Exception{
		testData.init();
	}
	@Test
	@Ignore
	public void testCrud() throws Exception {
		List<OAuthClient> initial = client.findAll().resultAsList(OAuthClient.class);

		OAuthClient obj = client.create("test-client-1", "test-client-1", "TEST1, TEST2", "read, write", "secret-1");
		String savedId = successAdd(client.add(obj));
		OAuthClient saved = client.get(savedId).resultAs(OAuthClient.class);
	
		compare(obj, saved);
		// get list
		List<OAuthClient> list1 = client.findAll().resultAsList(OAuthClient.class);
		Assert.assertEquals(initial.size() + 1, list1.size());

		// verify validations
		obj = client.create(null, "test-client-1", "TEST1, TEST2", "read, write", "secret-1");
		RestResponse resp = client.add(obj);

		Assert.assertTrue(resp.getErrorDetail() instanceof ValidationErrorDetail);
		ValidationErrorDetail errorDetail = (ValidationErrorDetail) resp.getErrorDetail();
		Assert.assertEquals(1, errorDetail.getErrors().size());
		Assert.assertEquals("clientId", errorDetail.getErrors().get(0).getField());
		String clientId = saved.getClientId();

		// update with null values.
		saved.setClientId(null);
		saved.setGrantTypes("password, refresh_token");
		resp = client.update(savedId, saved);
		// there should be errors because secret field cannot be updated
		Assert.assertTrue(resp.hasErrors());
		errorDetail = (ValidationErrorDetail) resp.getErrorDetail();
		Assert.assertTrue(hasValidationError(errorDetail, "UpdatePolicy", "secret"));
		// now set secret to null
		saved.setSecret(null);
		resp = client.update(savedId, saved);
		Assert.assertFalse(resp.hasErrors());

		saved = client.get(savedId).resultAs(OAuthClient.class);
		// client id is still the original
		Assert.assertEquals(clientId, saved.getClientId());

		resp = client.delete(savedId);
		// should delete successfully
		Assert.assertFalse(resp.hasErrors());

		// try to delete non-existing id
		resp = client.delete("xxxx11111");
		// should have errors
		Assert.assertTrue(resp.hasErrors());

		// should return a resource not found error
		Assert.assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, resp.getErrorDetail().getCode());
	}

	@Test
	@Ignore
	public void testPassword() throws Exception {
		OAuthClient obj = client.create("test-client-2", "test-client-2", "TEST1, TEST2", "read, write", "secret-2");
		String savedId = successAdd(client.add(obj));
		OAuthClient saved = client.get(savedId).resultAs(OAuthClient.class);
		comparePass(obj.getSecret(), saved.getSecret());

		// saving password with update is not allowed. It should throw an exception
		obj.setSecret("newSecret");
		try {
			success(client.update(savedId, obj));
			Assert.fail("Should throw an exception");
		} catch (RestException e) {
			//It is expected
		}
		success(client.updatePassword(savedId, obj.getSecret()));
		saved = client.get(savedId).resultAs(OAuthClient.class);
		comparePass(obj.getSecret(), saved.getSecret());	
	}

	private void compare(OAuthClient expected, OAuthClient actual) {
		Assert.assertEquals(expected.getClientId(), actual.getClientId());
		Assert.assertEquals(expected.getGrantTypes(), actual.getGrantTypes());
		Assert.assertEquals(expected.getRoles(), actual.getRoles());
		Assert.assertEquals(expected.getScopes(), actual.getScopes());
		Assert.assertEquals(expected.getAccessTokenValidityMs(), actual.getAccessTokenValidityMs());
		Assert.assertEquals(expected.getRefreshTokenValidityMs(), actual.getRefreshTokenValidityMs());

	}
	@Test
	public void testSecurity() throws Exception {
		/*
		 * 1. SysAdmin can manage all clients
		 * 2. ClientAdmin can manage only its own record
		 * 3. Reader and UserManager can only see the list of clients and get an individual client. It cannot do crud operations or change password
		 */
		testUserFunctions(TestData.USERNAME_SYSADMIN, TestData.USER_PASSWORD, true, true, true, true);
		testUserFunctions(TestData.USERNAME_CLIENT_ADMIN, TestData.USER_PASSWORD, false, true, true, false);
		testUserFunctions(TestData.USERNAME_USERADMIN, TestData.USER_PASSWORD, false, false, true, false);
		testUserFunctions(TestData.USERNAME_READER, TestData.USER_PASSWORD, false, false, true, false);
		testUserFunctions(TestData.USERNAME_NORMAL, TestData.USER_PASSWORD, false, false, false, false);
	}
}
