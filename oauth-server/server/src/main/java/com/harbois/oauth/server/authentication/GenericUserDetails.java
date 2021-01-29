package com.harbois.oauth.server.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class GenericUserDetails implements UserDetails {

	private static final long serialVersionUID = 2692870071807546712L;
	private String username;
	private String password;
	List<SimpleGrantedAuthority> authorities;

	public GenericUserDetails(String username, String password, Set<String> groups) {
		super();
		this.username = username;
		this.password = password;
		authorities = new ArrayList<>();
		for (String group : groups) {
			authorities.add(new SimpleGrantedAuthority(group));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}