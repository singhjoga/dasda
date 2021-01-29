package com.harbois.oauth.server.tests.funtionaltests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.oauth.server.api.v1.common.RestResponse;
import com.harbois.oauth.server.api.v1.groups.Group;
import com.harbois.oauth.server.api.v1.roles.Role;
import com.harbois.oauth.server.api.v1.settings.AppAuthSetting;
import com.harbois.oauth.server.api.v1.users.User;
import com.harbois.oauth.server.authentication.AuthSettings;
import com.harbois.oauth.server.tests.base.TestBase;
import com.harbois.oauth.server.tests.cleints.AppAuthSettingsClient;
import com.harbois.oauth.server.tests.cleints.GroupClient;
import com.harbois.oauth.server.tests.cleints.RoleClient;
import com.harbois.oauth.server.tests.cleints.UserClient;

public class AppAuthSettingsTest extends TestBase {

	@Autowired
	private AppAuthSettingsClient client;


	@Test
	public void testCrud() throws Exception {
		List<AppAuthSetting> initial = client.findAll().resultAsList(AppAuthSetting.class);

		AppAuthSetting obj = client.create("test-group-1", "test-group-1");
		obj.setDisplayOrder(50f);
		Long savedId = client.addAndGetId(obj);
		AppAuthSetting saved = client.get(savedId).resultAs(AppAuthSetting.class);
		compare(obj, saved);
	}
	
	private void compare(AppAuthSetting expected, AppAuthSetting actual) {
		Assert.assertEquals(expected.getName(), actual.getName());
		Assert.assertEquals(expected.getDescription(), actual.getDescription());
		Assert.assertEquals(expected.getClientId(), actual.getClientId());
		Assert.assertEquals(expected.getSettingsJson(), actual.getSettingsJson());
		Assert.assertEquals(expected.getProviderType(), actual.getProviderType());
		
	}
}
