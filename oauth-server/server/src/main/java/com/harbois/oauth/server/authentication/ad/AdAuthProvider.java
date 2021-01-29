package com.harbois.oauth.server.authentication.ad;

import java.util.Set;

import javax.naming.directory.DirContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.harbois.oauth.server.authentication.AuthenticationErrorType;
import com.harbois.oauth.server.authentication.AuthorizationFailureException;
import com.harbois.oauth.server.authentication.CustomAuthenticationAdapter;
import com.harbois.oauth.server.authentication.CustomAuthenticationProvider;
import com.harbois.oauth.server.authentication.GenericUserDetails;
import com.harbois.oauth.server.authentication.ldap.LdapAuthProvider;

public class AdAuthProvider  extends CustomAuthenticationAdapter<AdAuthSettings, DirContext> implements CustomAuthenticationProvider{
	private static final Logger LOG = LoggerFactory.getLogger(AdAuthProvider.class);	
	private AdAuthSettings settings;
	private AdService service;
	public AdAuthProvider(AdAuthSettings settings) {
		super(settings);
		this.settings = settings;
		this.service=new AdService(settings.getLdapUrl(),settings.getSearchBase(), settings.getSearchFilter(),settings.getDomainName());
	}
	@Override
	public DirContext authenticateUser(Authentication auth, String clientId)
			throws AuthenticationException, AuthorizationFailureException {
		LOG.info("Authenticating "+auth.getName());				
		return service.authenticate(auth.getName(), auth.getCredentials().toString());
	}

	@Override
	public Set<String> getUserRoles(DirContext context, Authentication authReq, String clientId)
			throws AuthenticationException, AuthorizationFailureException {
		LOG.info("Authorizing "+authReq.getName());		
		if (context == null) {
			//user was not authenticated with AD, use manager credentials to first authenticate
			context=service.authenticate(settings.getManagerDn(), settings.getManagerPassword());
		}
		return service.getUserGroups(context, authReq.getName());
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(settings.getManagerDn())) {
			return null;
		}
		DirContext context = service.authenticate(settings.getManagerDn(), settings.getManagerPassword());
		try {
			Set<String> groups = service.getUserGroups(context, username);
			return new GenericUserDetails(username, null, groups);
		} catch (AuthorizationFailureException e) {
			if (e.getErrorType() == AuthenticationErrorType.USER_NOT_FOUND) {
				return null;
			}else {
				throw e;
			}
		}
	}
}
