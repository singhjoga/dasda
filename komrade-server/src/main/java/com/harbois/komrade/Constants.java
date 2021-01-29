package com.harbois.komrade;

import java.nio.charset.Charset;

public interface Constants {
	String CLIENT_ID_KOMRADE_SERVER="komrade-server";
	String RESOURCE_ID="komrade-api";
	String ROLE_SYS_ADMIN="SystemAdministrator";
	String ROLE_CLIENT_ADMIN="ClientAdministrator";
	String ROLE_READER="Reader";
	String ROLE_USER_MANAGER="UserManager";
	String CLIENT_ID_GLOBAL="global";
	String CLIENT_ID_DEFAULT="default-app";
	Long ROLE_ID_SYS_ADMIN=1L;
	Long ROLE_ID_ClIENT_ADMIN=2L;
	Long ROLE_ID_USER_MANAGER=3l;
	Long ROLE_ID_READER=4L;
	Long GROUP_ID_SYSADMINS=1L;
	String HAS_ROLE_SYSADMIN="hasAnyAuthority('SystemAdministrator')";
	String HAS_ROLE_SYSADMIN_OR_CLIENTADMIN="hasAnyAuthority('SystemAdministrator','ClientAdministrator')";
	String[] ALL_ROLES = {ROLE_SYS_ADMIN,ROLE_CLIENT_ADMIN,ROLE_READER,ROLE_USER_MANAGER};
	String[] CLIENT_MGMT_ROLES = {ROLE_SYS_ADMIN,ROLE_CLIENT_ADMIN};
	String[] USER_MGMT_ROLES = {ROLE_SYS_ADMIN,ROLE_USER_MANAGER};
	
	Charset UTF8 = Charset.forName("UTF8");
}
