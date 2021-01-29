package com.harbois.oauth.authentication;

import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.harbois.oauth.authentication.AuthSettings.WhenRolesFound;
import com.harbois.oauth.authentication.AuthSettings.WhenRolesNotFound;
import com.harbois.oauth.authentication.AuthSettings.WhenUserNotFound;

public abstract class CustomAuthenticationAdapter<S extends AuthSettings, C> implements CustomAuthenticationProvider{
	
	private boolean initialized;
	
	private S settings;
	
	
	public S getSettings() {
		return settings;
	}

	public CustomAuthenticationAdapter(S settings) {
		super();
		this.settings = settings;
	}

	@Override
	public boolean canAuthenticate() {
		return settings.canAuthenticate();
	}

	@Override
	public boolean canAuthorize() {
		return settings.canAuthorize();
	}

	@Override
	public WhenUserNotFound getWhenUserNotFound() {
		return settings.getWhenUserNotFound();
	}

	@Override
	public WhenRolesNotFound getWhenRolesNotFound() {
		return settings.getWhenRolesNotFound();
	}

	@Override
	public WhenRolesFound getWhenRolesFound() {
		return settings.getWhenRolesFound();
	}
	@Override
	public Authentication authenticate(Authentication auth, String clientId) throws AuthenticationException, AuthorizationFailureException {
		boolean canAuthenticate = settings.canAuthenticate();
		boolean canAuthorize = settings.canAuthorize();
		//if already authenticated, no need to authenticate again
		if (auth.isAuthenticated()) {
			canAuthenticate=false;
		}else if (!canAuthenticate) {
			//user not authenticated and this provider also cannot authenticate
			return null;
		}
		if (!canAuthenticate && !canAuthorize) {
			//this provider is not suppose to authenticate or authorize
			return null;
		}
		Set<String> roles = Collections.emptySet();
		C ctx=null;
		if (canAuthenticate) {
			ctx = authenticateUser(auth, clientId);
		}
		
		if (canAuthorize) {
			roles = getUserRoles(ctx, auth, clientId);
		}
		
		return new AuthResponse(auth.getName(), auth.getCredentials().toString(), auth.getAuthorities(),roles);
	}
	
	public abstract C authenticateUser(Authentication auth, String clientId) throws AuthenticationException, AuthorizationFailureException;
	public abstract Set<String> getUserRoles(C context, Authentication authReq, String clientId) throws AuthenticationException, AuthorizationFailureException;
}
