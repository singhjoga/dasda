package com.harbois.komrade.runner.scm;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.harbois.komrade.runner.Password;
import com.harbois.komrade.runner.utils.CommonUtils;

public class GitCommandTest {
	private static final String URL_STR="https://git.tech.rz.db.de/dbs-devops-solutions/applications/cmdb/cmdb-server.git";
	private static final String BIN_PATH="D:/Git/bin/git.exe";
	private static final File GIT_FILE=new File(BIN_PATH);
	private static final String GIT_USER="jogagittoken";
	private static final String GIT_PASSWORD_STR="LTJQXU_ZDy8XR9fYEoom";
	private static final Password GIT_PASSWORD=new Password(GIT_PASSWORD_STR);
	
	private URL repoUrl;
	
	@Before
	public void init() {
		try {
			repoUrl = new URL(URL_STR);
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Test
	@Ignore
	public void testSetCredentialsStore() throws IOException, ScmException {
		GitCommand git = new GitCommand(GIT_FILE,repoUrl,GIT_USER,GIT_PASSWORD);
		git.setCredentialsStore(new URL("https://git.tech.rz.db.de/dbs-devops-solutions/applications/cmdb/cmdb-server.git"), "jogagittoken", GIT_PASSWORD);
	}
	@Test
	//@Ignore
	public void testZip() throws IOException, ScmException {
		GitCommand git = new GitCommand(GIT_FILE,repoUrl,GIT_USER,GIT_PASSWORD);
		File outDir = new File(CommonUtils.getTempDir().getAbsolutePath()+File.separator+"gittestt1");
		git.archive("refs/heads/master", "docs",outDir);
	}
	@Test
	@Ignore
	public void testCloneBare() throws IOException, ScmException {
		GitCommand git = new GitCommand(GIT_FILE,repoUrl,GIT_USER,GIT_PASSWORD);
		//URL url= new URL("https://jogagittoken:LTJQXU_ZDy8XR9fYEoom@git.tech.rz.db.de/dbs-devops-solutions/applications/cmdb/cmdb-server.git");
		File outDir = new File(CommonUtils.getTempDir().getAbsolutePath()+File.separator+"gittestt1");
		if (outDir.exists()) {
			outDir.delete();
		}
		git.cloneShallow( "2018.07.13", outDir);
	}
}
