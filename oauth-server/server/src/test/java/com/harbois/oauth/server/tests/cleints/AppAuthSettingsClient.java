package com.harbois.oauth.server.tests.cleints;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harbois.oauth.server.api.v1.common.RestResponse;
import com.harbois.oauth.server.api.v1.groups.Group;
import com.harbois.oauth.server.api.v1.roles.Role;
import com.harbois.oauth.server.api.v1.settings.AppAuthSetting;
import com.harbois.oauth.server.api.v1.settings.AuthProviderType;
import com.harbois.oauth.server.api.v1.users.User;
import com.harbois.oauth.server.authentication.AuthSettings.WhenRolesFound;
import com.harbois.oauth.server.authentication.AuthSettings.WhenRolesNotFound;
import com.harbois.oauth.server.authentication.AuthSettings.WhenUserNotFound;
import com.harbois.oauth.server.authentication.internaldb.InternalDbAuthSettings;
import com.harbois.oauth.server.authentication.internaldb.InternalDbAuthSettings.AuthorityType;
import com.harbois.oauth.server.tests.base.IdentifiableClient;
import com.harbois.oauth.server.tests.base.RestException;
@Service
public class AppAuthSettingsClient extends IdentifiableClient<AppAuthSetting, Long>{
	public static final String URL="/api/v1/authsettings";
	public AppAuthSettingsClient() {
		super(URL, AppAuthSetting.class, Long.class);
	}
	public AppAuthSetting create(String name, String description)  throws Exception{
		return create(name,description,"global");
	}
	public AppAuthSetting create(String name, String description, String clientId) throws Exception{
		InternalDbAuthSettings settings = new InternalDbAuthSettings();
		settings.setCanAuthenticate(true);
		settings.setCanAuthorize(true);
		settings.setReturnAuthorityType(AuthorityType.Roles);
		settings.setWhenRolesFound(WhenRolesFound.Continue);
		settings.setWhenRolesNotFound(WhenRolesNotFound.Continue);
		settings.setWhenUserNotFound(WhenUserNotFound.Continue);
		AppAuthSetting obj = new AppAuthSetting();
		obj.setDescription(description);
		obj.setName(name);
		obj.setClientId(clientId);
		obj.setProviderType(AuthProviderType.InternalDatabase);
		obj.setSettingsJson(asJsonString(settings));
		return obj;
	}

	@Override
	public AppAuthSetting createTestObject(String clientId) throws Exception{
		return create(UUID.randomUUID().toString(),"Test description",clientId);
	}
	
}
