package com.harbois.oauth.server.tests.cleints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.oauth.server.api.v1.common.Constants;

@Component
public class TestData {
	public static final String CLIENT_ID="test-app";
	public static final String USERNAME_SYSADMIN="test-sysadmin";
	public static final String USERNAME_CLIENT_ADMIN="test-client-admin";
	public static final String USERNAME_READER="test-reader";
	public static final String USERNAME_USERADMIN="test-useradmin";
	public static final String USERNAME_NORMAL="test-normal";
	public static final String USERNAME_GLOBAL_CLIENT_ADMIN="test-global-client-admin";
	public static final String USERNAME_GLOBAL_READER="test-global-reader";
	public static final String USERNAME_GLOBAL_USERADMIN="test-global-useradmin";
	public static final String USERNAME_GLOBAL_NORMAL="test-global-normal";
	public static final String CLIENT_SECRET="1212211";
	public static final String USER_PASSWORD="3343344343";
	
	@Autowired
	private ClientsClient clientsClient;
	@Autowired
	private UserClient userClient;
	@Autowired
	private GroupClient groupClient;
	@Autowired
	private RoleClient roleClient;
	
	private boolean isInitialised=false;
	
	public void init() throws Exception{
		if (isInitialised) return;
		isInitialised=true;
		
		//create a new client
		clientsClient.add(clientsClient.create(CLIENT_ID, "password", "ALL", "read,write", CLIENT_SECRET));
		
		//create users
		userClient.add(userClient.create(USERNAME_SYSADMIN, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_CLIENT_ADMIN, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_NORMAL, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_READER, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_USERADMIN, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_GLOBAL_CLIENT_ADMIN, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_GLOBAL_NORMAL, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_GLOBAL_READER, USER_PASSWORD));
		userClient.add(userClient.create(USERNAME_GLOBAL_USERADMIN, USER_PASSWORD));
		
		//Create groups for client
		Long clientAdminsGroupId = groupClient.addAndGetId(groupClient.create("TestClientAdmins", "Test Client Adnubs",CLIENT_ID));
		Long readersGroupId = groupClient.addAndGetId(groupClient.create("TestReaders", "Test Read Only Users",CLIENT_ID));
		Long userManaerGroupId = groupClient.addAndGetId(groupClient.create("TestUserManagerGroup", "Test UserManager Group",CLIENT_ID));
		Long normalGroupId = groupClient.addAndGetId(groupClient.create("TestNormalGroup", "Test Normal Group",CLIENT_ID));

		//create global groups
		Long globalClientAdminsGroupId = groupClient.addAndGetId(groupClient.create("GlobalTestClientAdmins", "Global Test Client Adnubs",Constants.CLIENT_ID_GLOBAL));
		Long globalReadersGroupId = groupClient.addAndGetId(groupClient.create("GlobalTestReaders", "Global Test Read Only Users",Constants.CLIENT_ID_GLOBAL));
		Long globalUserManaerGroupId = groupClient.addAndGetId(groupClient.create("GlobalTestUserManagerGroup", "Global Test UserManager Group",Constants.CLIENT_ID_GLOBAL));
		Long globalNormalGroupId = groupClient.addAndGetId(groupClient.create("GlobalTestNormalGroup", "Global Test Normal Group",Constants.CLIENT_ID_GLOBAL));
		
		//Create user defined roles
		Long globalRoleId = roleClient.addAndGetId(roleClient.create("GlobalTestNormalRole", " Global Test Normal Role",Constants.CLIENT_ID_GLOBAL));
		Long clientRoleId = roleClient.addAndGetId(roleClient.create("TestNormalRole", "Test Normal Role",CLIENT_ID));
		
		
		//Map Roles to Groups
		groupClient.attachRoles(clientAdminsGroupId, Constants.ROLE_ID_ClIENT_ADMIN);
		groupClient.attachRoles(globalClientAdminsGroupId, Constants.ROLE_ID_ClIENT_ADMIN);
		groupClient.attachRoles(readersGroupId, Constants.ROLE_ID_READER);
		groupClient.attachRoles(globalReadersGroupId, Constants.ROLE_ID_READER);
		groupClient.attachRoles(userManaerGroupId, Constants.ROLE_ID_USER_MANAGER);
		groupClient.attachRoles(globalUserManaerGroupId, Constants.ROLE_ID_USER_MANAGER);
		groupClient.attachRoles(normalGroupId, clientRoleId);
		groupClient.attachRoles(globalNormalGroupId, globalRoleId);
		
		//map users to groups
		groupClient.attachUsers(clientAdminsGroupId, USERNAME_CLIENT_ADMIN);
		groupClient.attachUsers(globalClientAdminsGroupId, USERNAME_GLOBAL_CLIENT_ADMIN);
		groupClient.attachUsers(readersGroupId, USERNAME_READER);
		groupClient.attachUsers(globalReadersGroupId, USERNAME_GLOBAL_READER);
		groupClient.attachUsers(userManaerGroupId, USERNAME_USERADMIN);
		groupClient.attachUsers(globalUserManaerGroupId, USERNAME_GLOBAL_USERADMIN);
		groupClient.attachUsers(normalGroupId, USERNAME_NORMAL);
		groupClient.attachUsers(globalNormalGroupId, USERNAME_GLOBAL_NORMAL);

		groupClient.attachUsers(Constants.GROUP_ID_SYSADMINS, USERNAME_SYSADMIN);
	
	}
}
