package com.harbois.oauth.authentication.jdbc;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.harbois.oauth.authentication.AuthenticationFailureException;
import com.harbois.oauth.authentication.AuthorizationFailureException;
import com.harbois.oauth.authentication.CustomAuthenticationAdapter;
import com.harbois.oauth.authentication.CustomAuthenticationProvider;
import com.harbois.oauth.authentication.GenericUserDetails;
import com.harbois.oauth.authentication.PasswordEncoderFactory;

public class JdbcAuthProvider extends CustomAuthenticationAdapter<JdbcAuthSettings, Void>
		implements CustomAuthenticationProvider {
	private static final Logger LOG = LoggerFactory.getLogger(JdbcAuthProvider.class);	
	private JdbcTemplate jdbcTemplate;
	private PasswordEncoder passwordEncoder;
	public JdbcAuthProvider(JdbcAuthSettings settings) {
		super(settings);
		jdbcTemplate = JdbcUtils.createJdbcTemplate(settings);
		passwordEncoder = PasswordEncoderFactory.createPasswordEncoder(settings.getPasswordEncoderSetting());
	}

	@Override
	public Void authenticateUser(Authentication auth, String clientId) throws AuthenticationException, AuthorizationFailureException {
		LOG.info("Authenticating "+auth.getName());				
		String username=auth.getName();
		String password=auth.getCredentials().toString();
		SqlRowSet rs = jdbcTemplate.queryForRowSet(getSettings().getAuthenticateSQL(), username);
		if (!rs.next()) {
			throw new AuthenticationFailureException("Authentication failed for user "+username+". User not found in database.");
		}
		String dbPassword = rs.getString(1);
		if (!passwordEncoder.matches(password, dbPassword)) {
			throw new AuthenticationFailureException("Authentication failed for user "+username+". Credentials mismatch.");
		}
				
		return null;
	}

	@Override
	public Set<String> getUserRoles(Void context, Authentication authReq, String clientId)
			throws AuthenticationException, AuthorizationFailureException {
		LOG.info("Authorizing "+authReq.getName());		
		String username=authReq.getName();
		Set<String> result = new TreeSet<>();
		SqlRowSet rs = jdbcTemplate.queryForRowSet(getSettings().getAuthenticateSQL(), username);
		while (rs.next()) {
			result.add(rs.getString(1));
		}
		
		return result;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SqlRowSet rs = jdbcTemplate.queryForRowSet(getSettings().getAuthenticateSQL(), username);
		if (!rs.next()) {
			return null;
		}
		
		return new GenericUserDetails(username, null, Collections.emptySet());
	}

}
