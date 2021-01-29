package com.harbois.komrade.runner.scm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harbois.komrade.runner.Password;
import com.harbois.komrade.runner.utils.CommonUtils;
import com.thetechnovator.common.java.exceptions.BusinessException;
import com.thetechnovator.common.java.utils.FileUtil;

public class GitScmProvider extends ScmProvider {
	private CredentialsProvider credentialProvider;
	private static final Logger LOG = LoggerFactory.getLogger(GitScmProvider.class);

	public GitScmProvider(String username, Password password) {
		this(username, password, null);
	}

	public GitScmProvider(String username, Password password, String baseUrl) {
		super(username, password, baseUrl);
		credentialProvider = new UsernamePasswordCredentialsProvider(username, password.toString());
	}

	@Override
	public void createTag(String sourceUrl, String tagName) throws ScmException {
		File tempDir=null;
		try {
			tempDir = Files.createTempDirectory("cicdgit").toFile();
			clone(sourceUrl, tempDir);
			createTag(tempDir, tagName);
		} catch (IOException e) {
			throw new ScmException(e.getMessage(), e);
		}finally {
			try {
				FileUtils.forceDelete(tempDir);
			} catch (IOException e) {
				throw new ScmException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void createTag(File workingDir, String tagName) throws ScmException {
		Git git=null;
		try {
			git = Git.open(workingDir);
			git.tag().setName(tagName).setMessage("Tag created by CICD Framework").call();
			git.push().setPushTags().setCredentialsProvider(credentialProvider).call();
		} catch (IOException | GitAPIException e) {
			throw new ScmException(e.getMessage(), e);
		}finally {
			close(git);
		}

	}

	@Override
	public void deleteTag(String tagName) throws ScmException {
		throw new ScmException("Method deleteTag not yet implemented");
	}

	@Override
	protected void createBranchInternal(String sourceUrl, String branchName) throws ScmException {
		Git git=null;
		File tempDir=null;
		try {
			tempDir = Files.createTempDirectory("cicdgit").toFile();
			clone(sourceUrl, tempDir);
			git = Git.open(tempDir);
			git.branchCreate().setName(branchName).call();
			git.push().setRemote("origin").setRefSpecs(new RefSpec(branchName+":"+branchName))
					.setCredentialsProvider(credentialProvider).call();
		} catch (IOException | GitAPIException e) {
			throw new ScmException(e.getMessage(), e);
		}finally {
			close(git);
			try {
				FileUtils.forceDelete(tempDir);
			} catch (IOException e) {
				throw new ScmException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void commit(File file) throws ScmException {
		Git git=null;
		try {
			File workingDir = file.isDirectory()?file:file.getParentFile();
			getSourceUrlFromDir(workingDir); //verify it to be a valid repo
			git = Git.open(workingDir);
			git.add().addFilepattern(".").call();
			git.commit().setMessage("Perform Release by CICD Framework").call();
			git.push().setCredentialsProvider(credentialProvider).call();
		} catch (IOException | GitAPIException e) {
			throw new ScmException(e.getMessage(), e);
		}finally {
			close(git);
		}
	}

	@Override
	public String getSourceUrlFromDir(File dir) throws ScmException {
		Git git=null;
		try {
			git = Git.open(dir);
			Repository repo = git.getRepository();
			String branch = repo.getBranch();
			if (branch == null || !repo.getFullBranch().startsWith("refs/heads")) {
				// it is probably a Tag checkout, in Git we cannot determine which Tag was
				// checked out
				throw new ScmException("Directory " + dir.getAbsolutePath() + " is not a valid Git branch repository");
			}
			String remoteUrl = repo.getConfig().getString("remote", "origin", "url");
			
			return remoteUrl+"/branches/"+branch;
		} catch (IOException e) {
			throw new ScmException(e.getMessage(), e);
		}finally {
			close(git);
		}
	}

	@Override
	public List<ScmEntry> getEntries(String url, boolean recursive) throws ScmException {
		throw new ScmException("Method getEntries not supported by Git SCM");
	}

	@Override
	public boolean relativePathExists(String relativePath) throws ScmException {
		throw new ScmException("Method relativePathExists not supported by Git SCM");
	}

	@Override
	public void checkout(File destFolder) throws ScmException {
		checkout(baseUrl, destFolder);
	}

	@Override
	public void checkout(String url, File destFolder) throws ScmException {
		clone(url, destFolder);
	}

	@Override
	public void export(File destFolder) throws ScmException {
		clone(baseUrl, destFolder);
	}

	@Override
	public void export(String url, File destFolder) throws ScmException {
		clone(url, destFolder);
	}

	@Override
	public void archive(File destFile) throws ScmException {
		archive(baseUrl,destFile);
	}

	@Override
	public void archive(String url, File destFile) throws ScmException {
		String refName = findRefNameFromUrl(url);
		String repoUrl = getRemoteUrl(url);
		cloneShallow(repoUrl, destFile.getParentFile(),refName);
		GitCommand git = getGitCommand(repoUrl);
		git.archive(refName, null, destFile.getParentFile(), destFile);
	}

	private void clone(String url, File destDir) throws ScmException {
		String ref = findRefFromUrl(url);
		String repoUrl = getRemoteUrl(url);
		clone(repoUrl, ref, destDir);
	}
	private void cloneShallow(String url, File destDir, String refName) throws ScmException {
		GitCommand git = getGitCommand(url);
		git.cloneShallow(refName, destDir);
	}
	private GitCommand getGitCommand(String repoUrl) throws ScmException{
		return new GitCommand(CommonUtils.toURL(repoUrl), username, password);
	}
	private String getRemoteUrl(String sourceUrl) {
		//source urls contain /tags/ or /branches/ suffixed to the remote url. Just return the remote url
		return StringUtils.substringBeforeLast(sourceUrl, ".git") + ".git";
	}
	private void close(Git git) {
		if (git != null) {
			git.close();
		}
	}
	private void clone(String repoUrl, String ref, File destDir) throws ScmException {
		Git git=null;
		try {
			LOG.info("Checking out branch " + ref + " from " + repoUrl);
		
			FileUtil.makeOrCleanDir(destDir);
			git = Git.cloneRepository().setBranchesToClone(Arrays.asList(ref)).setBranch(ref).setCredentialsProvider(credentialProvider)
					.setURI(repoUrl).setDirectory(destDir) //
					.call();
		} catch (GitAPIException e) {
			throw new ScmException(e.getMessage(), e);
		} catch (BusinessException e) {
			throw new ScmException(e.getMessage(), e);
		}finally {
			close(git);
		}
	}

	private static String findRefFromUrl(String url) throws ScmException {
		// URL should contain in the end tags/tag-name or branches/branch-name
		String name = StringUtils.substringAfterLast(url, "/branches/");
		if (StringUtils.isNoneEmpty(name)) {
			return "refs/heads/" + name;
		}
		// no branch, find tag
		name = StringUtils.substringAfterLast(url, "/tags/");
		if (StringUtils.isNoneEmpty(name)) {
			return "refs/tags/" + name;
		}

		throw new ScmException("Branch or tag not specifiied in " + url + ". URL should be suffixed with tags/tag-name or branches/branch-name");
	}
	private static String findRefNameFromUrl(String url) throws ScmException {
		// URL should contain in the end tags/tag-name or branches/branch-name
		String name = StringUtils.substringAfterLast(url, "/branches/");
		if (StringUtils.isNoneEmpty(name)) {
			return name;
		}
		// no branch, find tag
		name = StringUtils.substringAfterLast(url, "/tags/");
		if (StringUtils.isNoneEmpty(name)) {
			return name;
		}

		throw new ScmException("Branch or tag not specifiied in " + url + ". URL should be suffixed with tags/tag-name or branches/branch-name");
	}

	@Override
	public Map<String, List<ScmEntry>> getSourceTree(String url, boolean returnBranches, boolean returnTags, boolean trunk) throws ScmException {
		List<ScmEntry> tags = new ArrayList<ScmProvider.ScmEntry>();
		List<ScmEntry> branches = new ArrayList<ScmProvider.ScmEntry>();
		try {
			Collection<Ref> refs = Git.lsRemoteRepository().setRemote(url).setHeads(returnBranches).setTags(returnTags)
					.setCredentialsProvider(credentialProvider).call();
			for (Ref refObj : refs) {
				/*
				 * Sample ref names refs/heads/master refs/tags/1.0.5 refs/tags/1.5.0
				 * refs/tags/1.18.1 refs/tags/1.1.6 refs/heads/dev
				 */
				String refName = refObj.getName();
				String realName = StringUtils.substringAfterLast(refName, "/");
				ScmEntry entry = new ScmEntry();
				entry.setName(realName);
				if (refName.startsWith("refs/heads")) {
					// it is a branch
					entry.setUrl(url + "/branches/" + realName);
					branches.add(entry);
				} else if (refName.startsWith("refs/tags")) {
					// it is a branch
					entry.setUrl(url + "/tags/" + realName);
					tags.add(entry);
				}
			}
			Map<String, List<ScmEntry>> result = new TreeMap<String, List<ScmEntry>>();
			if (!tags.isEmpty()) {
				result.put("tags", tags);
			}
			if (!branches.isEmpty()) {
				result.put("branches", branches);
			}
			
			return result;
		} catch (GitAPIException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}
}
