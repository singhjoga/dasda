package com.harbois.komrade.v1.credentials.provider;

import java.util.List;
import java.util.Properties;

public interface CredentialsManager {
	public void init(Properties configProps) throws CredentialsException;
	public ProviderCredentialsType[] supportedCredentails();
	public String propertiesPrefix();
	/**
	 * @param key
	 * @param environmentCode
	 * @return null of credentails not found for the 'key'
	 * @throws CredentialsException
	 */
	public CredentialsEntry getCredetails(String key, String environmentCode) throws CredentialsException;
	public List<CredentialsEntry> getCredetails(List<String> keys, String environmentCode) throws CredentialsException;
	public void destroy() throws CredentialsException;
	/**
	 * Used for prefixing values
	 * @return
	 */
	public String shortName(); 
	public String name();
}
