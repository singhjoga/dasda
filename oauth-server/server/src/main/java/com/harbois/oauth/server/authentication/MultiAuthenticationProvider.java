package com.harbois.oauth.server.authentication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.harbois.oauth.server.api.v1.common.Constants;

@Component
public class MultiAuthenticationProvider implements AuthenticationProvider, UserDetailsService {
	private static final Logger LOG = LoggerFactory.getLogger(MultiAuthenticationProvider.class);

	@Autowired
	private AuthSettingsProvider settings;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		//Get the clientId from the currently authenticated user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String clientId = authentication.getName();
		
		Authentication authToken;
		Authentication result=auth;
		for (CustomAuthenticationProvider provider: settings.getProviders(clientId)) {
			try {
				authToken = result;  //pass the token returned from previous provider, to add up all roles
				result = provider.authenticate(authToken, clientId);
				if (provider.canAuthorize()) {
					//authorization was successful, check what to do next
					if (result.getAuthorities().isEmpty()) {
						//no roles found
						if (provider.getWhenRolesNotFound().returnFailure()) {
							throw new AuthenticationFailureException("Authentication failed for user "+auth.getName()+". No roles found.");
						}else if (provider.getWhenRolesNotFound().returnSuccess()) {
							return result;
						} //else continue 
					}else {
						if (provider.getWhenRolesFound().returnSuccess()) {
							return result;
						} //else continue
					}
				}
			} catch (AuthenticationFailureException e) {
				//authentication failure. We ignore the user not found errors to continue
				if (e.getErrorType() != AuthenticationErrorType.USER_NOT_FOUND) {
					throw e;
				}
			} catch (AuthorizationFailureException e) {
				//authorization failure. there are user defined settings
				if (e.getErrorType() == AuthenticationErrorType.USER_NOT_FOUND && provider.getWhenUserNotFound().returnFailure()) {
					throw e;
				}
			}
		
		
		}
		
		return result;

	}

	private List<? extends GrantedAuthority> stringList2Authority(List<String> stringList) {
		List<SimpleGrantedAuthority> result = new ArrayList<SimpleGrantedAuthority>();
		for (String str: stringList) {
			result.add(new SimpleGrantedAuthority(str));
		}
		
		return result;
	}
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String clientId = authentication.getName();
		UserDetails user;
		for (CustomAuthenticationProvider provider: settings.getProviders(clientId)) {
			try {
				user = provider.loadUserByUsername(username);
				if (user != null) {
					return user;
				}
			} catch (AuthenticationFailureException e) {
				//authentication failure. We ignore the user not found errors to continue
				if (e.getErrorType() != AuthenticationErrorType.USER_NOT_FOUND) {
					throw e;
				}
			} catch (AuthorizationFailureException e) {
				//authorization failure. there are user defined settings
				if (e.getErrorType() != AuthenticationErrorType.USER_NOT_FOUND) {
					throw e;
				}
			}
		}
		
		throw new UsernameNotFoundException("User "+username+" not found");
	}

}
