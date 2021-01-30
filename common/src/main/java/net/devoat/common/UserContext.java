package net.devoat.common;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class UserContext {
	private String username;
	private String clientId;
	private String accessToken;
	private boolean isSystemAdmin;
	private boolean isClientAdmin;
	private Set<String> roles;
	private static ThreadLocal<UserContext> instance = new ThreadLocal<UserContext>() {
		@Override
		protected UserContext initialValue() {
			return new UserContext();
		}
		
	};

	public UserContext() {
	
	}

	public void init() {
		//it gets called from ContextHandler, just to make sure to re-init the pooled object
		isSystemAdmin=false;
		isClientAdmin=false;
		username=null;
		clientId=null;
		accessToken=null;
		roles = new HashSet<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth==null) {
			return;
		}
		if (auth instanceof OAuth2Authentication) {
			OAuth2Authentication o2auth = (OAuth2Authentication)auth;
			this.username=o2auth.getName();
			this.clientId=o2auth.getOAuth2Request().getClientId();
			for (GrantedAuthority role: o2auth.getAuthorities()) {
				if (role.getAuthority().equals(Constants.ROLE_CLIENT_ADMIN)) {
					isClientAdmin=true;
				}else if (role.getAuthority().equals(Constants.ROLE_SYS_ADMIN)) {
					isSystemAdmin=true;
				}
				roles.add(role.getAuthority());
			}
		}
		
		if (auth.getDetails() instanceof OAuth2AuthenticationDetails) {
    		OAuth2AuthenticationDetails oauth = (OAuth2AuthenticationDetails)auth.getDetails();
    		this.accessToken= oauth.getTokenValue();
    	}
	}
	
	public static UserContext getInstance() {
		return instance.get();

	}
	public boolean hasAnyRole(String... roleAry) {
		for (String role: roleAry) {
			if (roles.contains(role)) {
				return true;
			}
		}
		return false;
	}
	public boolean hasAnyRole(Set<String> roleSet) {
		for (String role: roleSet) {
			if (roles.contains(role)) {
				return true;
			}
		}
		return false;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientId() {
		return clientId;
	}

	void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public boolean isSystemAdmin() {
		return isSystemAdmin;
	}

	void setSystemAdmin(boolean isSystemAdmin) {
		this.isSystemAdmin = isSystemAdmin;
	}

	public boolean isClientAdmin() {
		return isClientAdmin;
	}

	void setClientAdmin(boolean isClientAdmin) {
		this.isClientAdmin = isClientAdmin;
	}

	public Set<String> getRoles() {
		return roles;
	}
	
	
}

