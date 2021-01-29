package com.harbois.oauth.server.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthResponse extends AbstractAuthenticationToken{
	private String password;
	private String username;
	private static final long serialVersionUID = 1L;
	public AuthResponse(String username, String password, Collection<? extends GrantedAuthority> authorities, Set<String> roles) {
		super(AuthResponse.combine(authorities, roles));
		super.setAuthenticated(true);
		this.username=username;
		this.password=password;
	}
	
	public AuthResponse(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		this(username,password,authorities,Collections.emptySet());
	}

	@Override
	public Object getCredentials() {
		return password;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		throw new IllegalArgumentException("Method access not allowed");
	}
	
	private static List<SimpleGrantedAuthority> combine(Collection<? extends GrantedAuthority> authorities,Set<String> roles) {
		List<SimpleGrantedAuthority> result = new ArrayList<SimpleGrantedAuthority>();
		if (authorities != null) {
			for (GrantedAuthority ga: authorities) {
				result.add(new SimpleGrantedAuthority(ga.getAuthority()));
			}
		}
		if (roles != null) {
			for (String role: roles) {
				result.add(new SimpleGrantedAuthority(role));
			}
		}
		
		return result;
	}
}
