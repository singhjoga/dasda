package com.harbois.oauth.authentication.internaldb;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.harbois.oauth.api.v1.common.Constants;
import com.harbois.oauth.api.v1.groups.GroupService;
import com.harbois.oauth.api.v1.roles.RoleService;
import com.harbois.oauth.api.v1.users.User;
import com.harbois.oauth.api.v1.users.UserService;
import com.harbois.oauth.authentication.AuthenticationFailureException;
import com.harbois.oauth.authentication.AuthorizationFailureException;
import com.harbois.oauth.authentication.CustomAuthenticationAdapter;
import com.harbois.oauth.authentication.CustomAuthenticationProvider;
import com.harbois.oauth.authentication.GenericUserDetails;

public class InternalDbAuthProvider extends CustomAuthenticationAdapter<InternalDbAuthSettings, Void> implements CustomAuthenticationProvider {
	private static final Logger LOG = LoggerFactory.getLogger(InternalDbAuthProvider.class);
	private UserService userService;
	private RoleService roleService;
	private GroupService groupService;
	private BCryptPasswordEncoder passwordEncoder;

	public InternalDbAuthProvider(InternalDbAuthSettings settings, UserService userService, RoleService roleService, GroupService groupService, BCryptPasswordEncoder passwordEncoder) {
		super(settings);
		// userService =
		// Oauth2ServerApplication.applicationContext().getBeansOfType(UserService.class).values().iterator().next();
		// passwordEncoder =
		// Oauth2ServerApplication.applicationContext().getBeansOfType(BCryptPasswordEncoder.class).values().iterator().next();
		// roleService =
		// Oauth2ServerApplication.applicationContext().getBeansOfType(RoleService.class).values().iterator().next();
		// groupService =
		// Oauth2ServerApplication.applicationContext().getBeansOfType(GroupService.class).values().iterator().next();
		this.userService = userService;
		this.roleService = roleService;
		this.groupService = groupService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Void authenticateUser(Authentication auth, String clientId) throws AuthenticationException, AuthorizationFailureException {
		String username = auth.getName();
		String password = auth.getCredentials().toString();
		LOG.info("Authenticating " + username);
		User user = userService.findById(username);
		if (user == null) {
			throw new AuthenticationFailureException("Authentication failed for user " + username + ". User not found in database.");
		}
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new AuthenticationFailureException("Authentication failed for user " + username + ". Credentials mismatch.");
		}

		return null;
	}

	@Override
	public Set<String> getUserRoles(Void context, Authentication authReq, String clientId) throws AuthenticationException, AuthorizationFailureException {
		LOG.info("Authorizing " + authReq.getName());
		Set<String> result = new TreeSet<>();
		if (Constants.CLIENT_ID_OAUTH_SERVER.equals(clientId)) {
			// it is the UI. Return roles for web security to work
			roleService.findByUsername(authReq.getName(), clientId).stream().forEach(c -> result.add(c.getName()));
		} else {
			if (getSettings().getReturnAuthorityType().isGroups()) {
				groupService.findByUsername(authReq.getName(), clientId).stream().forEach(c -> result.add(c.getName()));
				// but also return system roles
			} else if (getSettings().getReturnAuthorityType().isRoles()) {
				roleService.findByUsername(authReq.getName(), clientId).stream().forEach(c -> result.add(c.getName()));
			} else {
				String msg = "Return permissions is not yet implemented";
				LOG.error(msg);
				throw new AuthorizationFailureException(msg);
			}
		}
		addInheritedRoles(result);
		return result;
	}
	private void addInheritedRoles(Set<String> assignedRoles) {
		if (assignedRoles.contains(Constants.ROLE_SYS_ADMIN)) {
			//sys admin has all other roles
			checkAndAddRole(assignedRoles, Constants.ROLE_CLIENT_ADMIN);
			checkAndAddRole(assignedRoles, Constants.ROLE_USER_MANAGER);
		}
		if (assignedRoles.contains(Constants.ROLE_CLIENT_ADMIN)) {
			//client admin is also a reader
			checkAndAddRole(assignedRoles, Constants.ROLE_READER);
		}	
		if (assignedRoles.contains(Constants.ROLE_USER_MANAGER)) {
			//user manager is also a reader
			checkAndAddRole(assignedRoles, Constants.ROLE_READER);
		}
	}
	
	private void checkAndAddRole(Set<String> assignedRoles, String role) {
		if (!assignedRoles.contains(role)){
			assignedRoles.add(role);
		}
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findById(username);
		if (user == null) {
			return null;
		}
		return new GenericUserDetails(username, null, Collections.emptySet());
	}

}
