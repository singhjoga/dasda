package com.dbsystel.devops.java.common;

import java.util.ArrayList;
import java.util.List;

public class KeePassConfig {
	private String gitUserName;
	private String gitPassword;
	private List<RepoConfig> repos = new ArrayList<>();
	private List<Integer> nos;
	public String getGitUserName() {
		return gitUserName;
	}

	public void setGitUserName(String gitUserName) {
		this.gitUserName = gitUserName;
	}

	public String getGitPassword() {
		return gitPassword;
	}

	public void setGitPassword(String gitPassword) {
		this.gitPassword = gitPassword;
	}

	public List<RepoConfig> getRepos() {
		return repos;
	}

	public void setRepos(List<RepoConfig> repos) {
		this.repos = repos;
	}

	public List<Integer> getNos() {
		return nos;
	}

	public void setNos(List<Integer> nos) {
		this.nos = nos;
	}

	public static class RepoConfig {
		private String url;
		private List<FileConfig> files;
		private List<String> tags;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public List<FileConfig> getFiles() {
			return files;
		}
		public void setFiles(List<FileConfig> files) {
			this.files = files;
		}
		public List<String> getTags() {
			return tags;
		}
		public void setTags(List<String> tags) {
			this.tags = tags;
		}
		
	}
	
	public static class FileConfig {
		private String name;
		private String envcodes;
		private String password;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEnvcodes() {
			return envcodes;
		}
		public void setEnvcodes(String envCodes) {
			this.envcodes = envCodes;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
	}
}
