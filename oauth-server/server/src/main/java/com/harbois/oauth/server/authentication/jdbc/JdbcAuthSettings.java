package com.harbois.oauth.server.authentication.jdbc;

import com.harbois.oauth.server.authentication.AuthSettings;
import com.harbois.oauth.server.authentication.PasswordEncoderSetting;

public class JdbcAuthSettings extends AuthSettings{
	private String jdbcUrl;
	private String driverClass;
	private String username;
	private String password;
	private String authenticateSQL;
	private String authorizeSQL;
	private PasswordEncoderSetting passwordEncoderSetting;
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuthenticateSQL() {
		return authenticateSQL;
	}
	public void setAuthenticateSQL(String authenticateSQL) {
		this.authenticateSQL = authenticateSQL;
	}
	public String getAuthorizeSQL() {
		return authorizeSQL;
	}
	public void setAuthorizeSQL(String authorizeSQL) {
		this.authorizeSQL = authorizeSQL;
	}
	public PasswordEncoderSetting getPasswordEncoderSetting() {
		return passwordEncoderSetting;
	}
	public void setPasswordEncoderSetting(PasswordEncoderSetting passwordEncoderSetting) {
		this.passwordEncoderSetting = passwordEncoderSetting;
	}
	
}
