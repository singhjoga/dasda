package com.harbois.oauth.server.tests.oauth;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.spi.NamingManager;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticator;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

public class ADTest {

	@Test
	@Ignore
	public void adTest() {
		try {
			getLdapServers("bku.db.de");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("JogaSingh", "Missing$Gallus");
		ActiveDirectoryLdapAuthenticationProvider ap = new ActiveDirectoryLdapAuthenticationProvider("bku.db.de", "ldaps://bku.db.de");
		ap.setSearchFilter("(&(objectClass=user)(sAMAccountName={1}))");

		ap.authenticate(auth);
	}
	
	@Test
	public void ldapTemplateTest() throws Exception{
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("JogaSingh", "Missing$Gallus");
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldaps://bku.db.de");
		//set manager Dn and Password
		contextSource.afterPropertiesSet();
		AbstractLdapAuthenticator ldapAuthenticator = new BindAuthenticator(contextSource);
		String[] userDns = new String[] {"sAMAccountName=JogaSingh@bku.db.de"};
		ldapAuthenticator.setUserDnPatterns(userDns);
		//LdapUserSearch userSearch = new FilterBasedLdapUserSearch("", userSearchFilter,
		//		contextSource);
		//ldapAuthenticator.setUserSearch(userSearch);
		ldapAuthenticator.afterPropertiesSet();
		ldapAuthenticator.authenticate(auth);
	}
	private Collection<String> getLdapServers(final String ldapDomain) throws NamingException {
		final Collection<String> serverRecords = getSRVRecords(ldapDomain);
		final Collection<String> serverNames = new LinkedHashSet<>();
		for (final String s : serverRecords) {
			String ldapServer = "ldaps://" + s.substring(s.lastIndexOf(' ') + 1, s.length() - 1);
			System.out.println("Server: " + ldapServer);
			serverNames.add(ldapServer);
		}
		return serverNames;
	}

	private Collection<String> getSRVRecords(final String ldapDomain) throws NamingException {
		final DirContext context = (DirContext) NamingManager.getURLContext("dns", new Hashtable<String, Object>());
		final String ldapDNSUrl = "dns:///_ldap._tcp." + ldapDomain;
		final String[] attrIds = { "SRV" };
		final Attributes attributes = context.getAttributes(ldapDNSUrl, attrIds);
		final Attribute servers = attributes.get("SRV");
		final Collection<String> serverRecords = new TreeSet<>();
		for (int i = 0; i < servers.size(); i++)
			serverRecords.add((String) servers.get(i));
		return serverRecords;
	}
}
