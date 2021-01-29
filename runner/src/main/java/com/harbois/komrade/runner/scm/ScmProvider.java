package com.harbois.komrade.runner.scm;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.harbois.komrade.runner.Password;
import com.harbois.komrade.runner.utils.EnumUtil;

public abstract class ScmProvider {
	public static final String TIMESTAMP_FORMAT = "yyyyMMdd.HHmmss";
	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
	
	protected String username;
	protected Password password;
	protected String baseUrl;
	
	public ScmProvider(String username, Password password, String baseUrl) {
		super();
		this.username = username;
		this.password = password;
		this.baseUrl = baseUrl;
	}
	public ScmProvider(String username, Password password) {
		this(username, password, null);
	}

	protected String getTimestamp() {
		return timestampFormat.format(new Date());
	}
	public void createBranch(String sourceUrl, String branchName) throws ScmException {
		//find the pom.xml and set the scm properties properly
		//TODO:
		createBranchInternal(sourceUrl, branchName);
	}
		
	public abstract void createTag(String sourceUrl, String tagName) throws ScmException;
	public abstract void createTag(File workingDir, String tagName) throws ScmException;
	public abstract void deleteTag(String tagName) throws ScmException;
	protected abstract void createBranchInternal(String sourceUrl, String branchName) throws ScmException;
	public abstract void commit(File file) throws ScmException;
	public abstract String getSourceUrlFromDir(File dir) throws ScmException;
	public abstract List<ScmEntry> getEntries(String url, boolean recursive) throws ScmException;
	public abstract boolean relativePathExists(String relativePath) throws ScmException;
	public abstract void checkout(File destFolder) throws ScmException;
	public abstract void checkout(String url, File destFolder) throws ScmException;
	public abstract void export(File destFolder) throws ScmException;
	public abstract void export(String url, File destFolder) throws ScmException;
	public abstract void archive(File destFile) throws ScmException;
	public abstract void archive(String url, File destFile) throws ScmException;

	public abstract Map<String,List<ScmEntry>> getSourceTree(String url, boolean braches, boolean tags, boolean trunk) throws ScmException;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Password getPassword() {
		return password;
	}
	public void setPassword(Password password) {
		this.password = password;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public static ScmProvider getInstance(ProviderType type, String username, Password password, String baseUrl) {
		if (type == ProviderType.svn) {
			return new SvnScmProvider(username, password, baseUrl);
		}else if (type == ProviderType.git) {
			return new GitScmProvider(username, password, baseUrl);			
		}
		
		return null;
	}
	public static ScmProvider getInstance(ProviderType type, String username, Password password) {
		return getInstance(type, username, password, null);
	}	
	
	public static enum ProviderType {
		svn, //
		git;
		public static boolean isValidValue(String strValue) {
			return EnumUtil.isValidValue(ProviderType.class, strValue);
		}
		
		public static String toDelimitedString() {
			return EnumUtil.toDelimitedString(ProviderType.class);
		}		
	}
	
	public static enum ScmRepoStructure {
		single_project, //
		multi_project;
		public static boolean isValidValue(String strValue) {
			return EnumUtil.isValidValue(ScmRepoStructure.class, strValue);
		}
		
		public static String toDelimitedString() {
			return EnumUtil.toDelimitedString(ScmRepoStructure.class);
		}	
	}
	public static class ScmEntry {
		  private String name;
		  private String url;
		  private String lastUpdateUser;
		  private Date lastUpdateDate;
		  private String revision;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getLastUpdateUser() {
			return lastUpdateUser;
		}
		public void setLastUpdateUser(String lastUpdateUser) {
			this.lastUpdateUser = lastUpdateUser;
		}
		public Date getLastUpdateDate() {
			return lastUpdateDate;
		}
		public void setLastUpdateDate(Date lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
		}
		public String getRevision() {
			return revision;
		}
		public void setRevision(String revision) {
			this.revision = revision;
		}
		  
	}
	protected void sortScmEntries(List<ScmEntry> entries) {
		Collections.sort(entries, new ScmDateComparator());
	}
	public static class ScmDateComparator implements Comparator<ScmEntry> {

	    @Override
	    public int compare(ScmEntry o1, ScmEntry o2) {
	        // Order Descending.
	    	if (o2.getLastUpdateDate().equals(o1.getLastUpdateDate())) {
	    		//If dates are equal, sort on name
	    		return o2.getName().compareTo(o1.getName());
	    	} else {
	    		return o2.getLastUpdateDate().compareTo(o1.getLastUpdateDate());
	    	}
	    }

	}
}
