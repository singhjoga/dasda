package com.harbois.komrade.settings.credmgmt;

import java.util.ArrayList;
import java.util.List;

public class KeePassSettings {
	private String scmCredentialsid;
	private List<ScmRepo> scmRepos = new ArrayList<>();

	public String getScmCredentialsid() {
		return scmCredentialsid;
	}

	public void setScmCredentialsid(String scmCredentialsid) {
		this.scmCredentialsid = scmCredentialsid;
	}

	public List<ScmRepo> getScmRepos() {
		return scmRepos;
	}

	public void setScmRepos(List<ScmRepo> scmRepos) {
		this.scmRepos = scmRepos;
	}

	public static class ScmRepo {
		private String scmUrl;
		private List<KeePassFile> keepassFiles;
		public String getScmUrl() {
			return scmUrl;
		}
		public void setScmUrl(String scmUrl) {
			this.scmUrl = scmUrl;
		}
		public List<KeePassFile> getKeepassFiles() {
			return keepassFiles;
		}
		public void setKeepassFiles(List<KeePassFile> keepassFiles) {
			this.keepassFiles = keepassFiles;
		}
		
	}
	
	public static class KeePassFile {
		private String fileName;
		private String targetEnvCodes;
		private String filePassword;
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getTargetEnvCodes() {
			return targetEnvCodes;
		}
		public void setTargetEnvCodes(String targetEnvCodes) {
			this.targetEnvCodes = targetEnvCodes;
		}
		public String getFilePassword() {
			return filePassword;
		}
		public void setFilePassword(String filePassword) {
			this.filePassword = filePassword;
		}
		
	}
}
