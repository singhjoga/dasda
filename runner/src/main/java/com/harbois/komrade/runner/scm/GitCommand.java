package com.harbois.komrade.runner.scm;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.harbois.komrade.runner.Constants;
import com.harbois.komrade.runner.Password;
import com.harbois.komrade.runner.exception.CommandExectionException;
import com.harbois.komrade.runner.processrunner.AbstractCommandlineApp;
import com.harbois.komrade.runner.processrunner.CommandParameter;
import com.harbois.komrade.runner.processrunner.StringParameter;
import com.harbois.komrade.runner.processrunner.UrlParameter;
import com.harbois.komrade.runner.utils.CommonUtils;

public class GitCommand extends AbstractCommandlineApp{
	public static final String GIT_BIN_PATH_PROP="scm.git.bin.path";
	private URL repoUrl;
	
	public GitCommand(File processFile, URL repoUrl, String username, Password password) throws ScmException {
		super(processFile);
		this.repoUrl = getUrlWithAuthInfo(repoUrl, username, password);
		init();
	}
	public GitCommand(URL repoUrl, String username, Password password) throws ScmException {
		this(new File(System.getProperty(GIT_BIN_PATH_PROP)),repoUrl,username,password);
	}
	public void init() throws ScmException{
		runCommand("config --global advice.detachedHead false");
	}
	public void setCredentialsStore(URL repoUrl, String username, Password password) throws ScmException{
		String currDir= CommonUtils.getWorkingDir().getAbsolutePath();
		//git serches credentials in $XDG_CONFIG_HOME/git/credentials
		//create the dir and put credentials there
		File credFile = new File(currDir+File.separator+"git"+File.separator+"credentials"+File.separator+".git-credentials");
		credFile.getParentFile().mkdirs();
		//getEnvironment().put("XDG_CONFIG_HOME", currDir);
		String userInfo;
		try {
			//userInfo = URLEncoder.encode(username,Constants.UTF8_STR)+":"+URLEncoder.encode(password.toString(),Constants.UTF8_STR);
			userInfo = username+":"+password.toString();
			URI url = new URI(repoUrl.getProtocol(), userInfo, repoUrl.getHost(), repoUrl.getPort(), null, repoUrl.getQuery(), repoUrl.getRef());
			FileUtils.write(credFile, url.toString(),Constants.UTF8_STR);
		} catch (URISyntaxException | IOException e) {
			throw new ScmException(e);
		}
		runCommand("config --global advice.detachedHead false");
		runCommand("config --global credential.helper store");
	}
	public File archive(String ref, String path, File outDir) throws ScmException{
		cloneShallow(ref, outDir);
		File outFile= new File(outDir+File.separator+"repo-contents.zip");
		return archive(ref, path, outDir, outFile);
	}
	public File archive(String ref, String path, File repoDir, File outputFile) throws ScmException{
		List<CommandParameter<?>> params = string2StringParameterList("archive -o "+outputFile.getAbsolutePath());
		params.add(new StringParameter(ref));
		if (StringUtils.isNotEmpty(path)) {
			params.add(new StringParameter(path));			
		}
		setWorkingDir(repoDir);
		runCommand(params);
		
		return outputFile;
	}
	public void cloneShallow(String ref, File targetDir) throws ScmException{
		List<CommandParameter<?>> params = string2StringParameterList("clone --depth 1 --shallow-submodules --no-tags --single-branch --branch "+ref);
		
		params.add(new UrlParameter(repoUrl));
		params.add(new StringParameter(targetDir.getAbsolutePath()));
		runCommand(params);
	}
	
	private void runCommand(String cmd) throws ScmException {
		List<CommandParameter<?>> params = string2StringParameterList(cmd);
		runCommand(params);
	}
	private void runCommand(List<CommandParameter<?>> params) throws ScmException {
		try {
			int exitCode=super.execute(params);
			if (exitCode != 0) {
				throw new ScmException("Git failed: "+toDisplayString(params));
			}
		} catch (CommandExectionException e) {
			throw new ScmException(e);
		}
	}
	private URL getUrlWithAuthInfo(URL url, String username, Password password) {
		String userInfo = username+":"+password.toString();
		URI newUrl;
		try {
			newUrl = new URI(url.getProtocol(), userInfo, url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			return newUrl.toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			throw new IllegalStateException();
		}
	}
}
