package com.harbois.oauth.server.authentication.ldap;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import com.harbois.oauth.server.authentication.AuthorizationFailureException;
import com.harbois.oauth.server.authentication.CustomAuthenticationAdapter;
import com.harbois.oauth.server.authentication.CustomAuthenticationProvider;

public class LdapAuthProvider extends CustomAuthenticationAdapter<LdapAuthSettings, Authentication> implements CustomAuthenticationProvider{
	private static final Logger LOG = LoggerFactory.getLogger(LdapAuthProvider.class);
	private LdapAuthSettings settings;
	private AuthenticationProvider authProvider;
	public LdapAuthProvider(LdapAuthSettings settings) {
		super(settings);
		this.settings = settings;
		LdapAuthManagerBuilder builder = new LdapAuthManagerBuilder(settings);
		try {
			builder.build();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		this.authProvider=builder.getAuthProvider();
	}

	@Override
	public Authentication authenticateUser(Authentication auth, String clientId)
			throws AuthenticationException, AuthorizationFailureException {
		LOG.info("Authenticating "+auth.getName());		
		return authProvider.authenticate(auth);
	}

	@Override
	public Set<String> getUserRoles(Authentication context, Authentication authReq, String clientId)
			throws AuthenticationException, AuthorizationFailureException {
		LOG.info("Authorizing "+authReq.getName());
		if (context == null) {
			//user is not authenticated already with this provider
			context= authProvider.authenticate(authReq);
		}
		Set<String> roles = new TreeSet<>();
		context.getAuthorities().stream().forEach(c -> roles.add(c.getAuthority()));
		return roles;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		return null;
	}
	private static class LdapAuthManagerBuilder implements ProviderManagerBuilder<LdapAuthManagerBuilder> {
		private AuthenticationProvider authProvider;
		private LdapAuthSettings settings;
		
		public LdapAuthManagerBuilder(LdapAuthSettings settings) {
			super();
			this.settings = settings;
		}

		public AuthenticationProvider getAuthProvider() {
			return authProvider;
		}

		@Override
		public AuthenticationManager build() throws Exception {
			LdapAuthenticationProviderConfigurer<LdapAuthManagerBuilder> configurer = new LdapAuthenticationProviderConfigurer<>();
			DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(settings.getLdapUrl());
			if (StringUtils.isNotEmpty(settings.getManagerDn())) {
				contextSource.setUserDn(settings.getManagerDn());
				contextSource.setPassword(settings.getManagerPassword());
			}
			contextSource.afterPropertiesSet();
			
			configurer.groupSearchBase(settings.getGroupSearchBase())
				.groupSearchFilter(settings.getGroupSearchFilter())
				.userDnPatterns(settings.getUserDnPattern())
				.userSearchBase(settings.getUserSearchBase())
				.userSearchFilter(settings.getUserSearchFilter())
				.groupRoleAttribute(settings.getGroupRoleAttribute())
				.groupSearchBase(settings.getGroupSearchBase())
				.groupSearchFilter(settings.getGroupSearchFilter())
				.contextSource(contextSource);
			configurer.configure(this);
			return null;
		}

		@Override
		public LdapAuthManagerBuilder authenticationProvider(AuthenticationProvider authProvider) {
			this.authProvider=authProvider;
			return this;
		}
		
	}
}
