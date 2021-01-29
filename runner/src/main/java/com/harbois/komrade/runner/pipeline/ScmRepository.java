package com.harbois.komrade.runner.pipeline;

public class ScmRepository {
	private String repositoryUrl;
	private String path;
	private String ref;
	private String provider;
	private Credentials credentials;
	private String checkoutPath;	
	public String getRepositoryUrl() {
		return repositoryUrl;
	}
	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getCheckoutPath() {
		return checkoutPath;
	}
	public void setCheckoutPath(String checkoutPath) {
		this.checkoutPath = checkoutPath;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	
}
