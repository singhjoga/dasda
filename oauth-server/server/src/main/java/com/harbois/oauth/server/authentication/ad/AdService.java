package com.harbois.oauth.server.authentication.ad;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harbois.oauth.server.authentication.AuthenticationErrorType;
import com.harbois.oauth.server.authentication.AuthenticationFailureException;
import com.harbois.oauth.server.authentication.AuthorizationFailureException;

public class AdService {

	private static final Logger LOG = LoggerFactory.getLogger(AdService.class);

	private final static Map<String, AuthenticationErrorType> errorCodesMap = new HashMap<String, AuthenticationErrorType>();
	private static final SearchControls userSearchCtls;

	static {
		errorCodesMap.put("525", AuthenticationErrorType.USER_NOT_FOUND);
		errorCodesMap.put("52e", AuthenticationErrorType.INVALID_CREDENTIALS);
		errorCodesMap.put("530", AuthenticationErrorType.NOT_PERMITTED_TIME);
		errorCodesMap.put("531", AuthenticationErrorType.NOT_PERMITTED_MACHINE);
		errorCodesMap.put("532", AuthenticationErrorType.PASSWORD_EXPIRED);
		errorCodesMap.put("533", AuthenticationErrorType.ACCOUNT_DISABLED);
		errorCodesMap.put("701", AuthenticationErrorType.ACCOUNT_EXPIRED);
		errorCodesMap.put("773", AuthenticationErrorType.PASSWORD_RESET);
		errorCodesMap.put("775", AuthenticationErrorType.ACCOUNT_LOCKED);

		userSearchCtls = new SearchControls();
		userSearchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	}
	private String ldapUrl;
	private String searchBase;
	private String searchFilter;
	private String domain;
	public AdService(String ldapUrl, String searchBase, String searchFilter,String domain) {
		super();
		this.ldapUrl = ldapUrl;
		this.searchBase = searchBase;
		this.searchFilter = searchFilter;
		this.domain=domain;
	}

	public DirContext authenticate(String username, final String password) throws AuthenticationFailureException {
		final Hashtable<String, Object> env = new Hashtable<>();
		String domainUsername=createDomainUser(username);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.REFERRAL, "follow");
		env.put("com.sun.jndi.ldap.connect.timeout", "2000");
		env.put(Context.SECURITY_PRINCIPAL, domainUsername);
		env.put(Context.SECURITY_CREDENTIALS, password);

		DirContext ctx = null;
		env.put(Context.PROVIDER_URL, ldapUrl);
		try {
			ctx = new InitialLdapContext(env, null);
			LOG.info("Authenticated user " + domainUsername + " on server " + ldapUrl);
			return ctx;
		} catch (AuthenticationException e) {
			AuthenticationErrorType errorType = getAuthenticationError(e);
			String msg = errorType == AuthenticationErrorType.UNKNOWN?e.getMessage():errorType.getMessage();
			throw new AuthenticationFailureException(msg, errorType, e);
		} catch (CommunicationException e) {
			LOG.error("Communication error: " + e.getRootCause().getMessage());
			throw new AuthenticationFailureException(e.getMessage(), AuthenticationErrorType.COMMUNICATION_ERROR, e);
		} catch (NamingException e) {
			throw new AuthenticationFailureException(e.getMessage(), AuthenticationErrorType.UNKNOWN, e);
		}
	}

	public Set<String> getUserGroups(DirContext ctx, String username) throws AuthenticationFailureException {
		try {
			String domainUsername=createDomainUser(username);
			Object[] param = { username,domainUsername };
			Set<String> groups = new TreeSet<String>();
			NamingEnumeration<SearchResult> searchResult = ctx.search(searchBase, searchFilter, param, userSearchCtls);
			if (!searchResult.hasMoreElements()) {
				//user not found
				throw new AuthorizationFailureException("User '"+username+"' not found", AuthenticationErrorType.USER_NOT_FOUND);
			}
			while (searchResult.hasMoreElements()) {
				Attributes attrs = searchResult.next().getAttributes();
				if (attrs == null) {
					continue;
				}
				Attribute groupAttr;

				groupAttr = attrs.get("memberOf");

				for (int i = 0; i < groupAttr.size(); i++) {
					String dn = (String) groupAttr.get(i);
					String group = dn.replaceAll("CN=([^,]*),.*", "$1");
					groups.add(group);
				}

			}

			return groups;
		} catch (NamingException e) {
			//throw new AuthenticationFailureException(e.getMessage(), AuthenticationErrorType.UNKNOWN, e);
			AuthenticationErrorType errorType = getAuthenticationError(e);
			String msg = errorType == AuthenticationErrorType.UNKNOWN?e.getMessage():errorType.getMessage();
			LOG.error("Error getting groups: " + msg, e);
			throw new AuthorizationFailureException(msg, errorType, e);

		}
	}

	public void close(final DirContext ctx) {
		if (ctx != null)
			try {
				ctx.close();
			} catch (final NamingException e) {
			}
	}

	private AuthenticationErrorType getAuthenticationError(NamingException e) {
		String code = StringUtils.substringBetween(e.getMessage(), ", data ", ", v");
		if (code != null && errorCodesMap.containsKey(code)) {
			return errorCodesMap.get(code);
		} else {
			return AuthenticationErrorType.UNKNOWN;
		}
	}
	
	private String createDomainUser(String username) {
		if (domain == null || username.toLowerCase().endsWith(domain)) {
			return username;
		}

		return username + "@" + domain;
	}
}
