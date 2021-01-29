package com.harbois.oauth.server.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.harbois.oauth.server.authentication.AuthSettings.WhenRolesFound;
import com.harbois.oauth.server.authentication.AuthSettings.WhenRolesNotFound;
import com.harbois.oauth.server.authentication.AuthSettings.WhenUserNotFound;

public interface CustomAuthenticationProvider extends UserDetailsService{
	public boolean canAuthenticate();
	public boolean canAuthorize();

	public WhenUserNotFound getWhenUserNotFound();
	public WhenRolesNotFound getWhenRolesNotFound();
	public WhenRolesFound getWhenRolesFound();
	public Authentication authenticate(Authentication auth, String clientId) throws AuthenticationException, AuthorizationFailureException;
	
}
