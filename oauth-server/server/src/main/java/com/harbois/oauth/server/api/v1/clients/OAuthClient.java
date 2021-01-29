package com.harbois.oauth.server.api.v1.clients;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.harbois.oauth.server.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.server.validation.annotations.NotNullable;
import com.harbois.oauth.server.validation.annotations.UpdatePolicy;

@Entity(name="OAUTH_CLIENT_DETAILS")
public class OAuthClient implements IdentifiableEntity<String>{

	@Id
	@Column(name="CLIENT_ID")
	@NotNullable
	private String clientId;
	@Column(name="CLIENT_SECRET")
	@NotNullable
	@UpdatePolicy(updateable=false)
	private String secret;
	@Column(name="SCOPE")
	@NotNullable
	private String scopes;
	@Column(name="AUTHORIZED_GRANT_TYPES")
	@NotNullable
	private String grantTypes;
	@Column(name="AUTHORITIES")
	private String roles;
	@Column(name="ACCESS_TOKEN_VALIDITY")
	@NotNullable
	private Long accessTokenValidityMs;
	@Column(name="REFRESH_TOKEN_VALIDITY")
	@NotNullable
	private Long refreshTokenValidityMs;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getScopes() {
		return scopes;
	}
	public void setScopes(String scopes) {
		this.scopes = scopes;
	}
	public String getGrantTypes() {
		return grantTypes;
	}
	public void setGrantTypes(String grantTypes) {
		this.grantTypes = grantTypes;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public Long getAccessTokenValidityMs() {
		return accessTokenValidityMs;
	}
	public void setAccessTokenValidityMs(Long accessTokenValidityMs) {
		this.accessTokenValidityMs = accessTokenValidityMs;
	}
	public Long getRefreshTokenValidityMs() {
		return refreshTokenValidityMs;
	}
	public void setRefreshTokenValidityMs(Long refreshTokenValidityMs) {
		this.refreshTokenValidityMs = refreshTokenValidityMs;
	}
	@Override
	public String getId() {
		return clientId;
	}
	@Override
	public void setId(String id) {
		this.clientId=id;
	}
	
	
}
