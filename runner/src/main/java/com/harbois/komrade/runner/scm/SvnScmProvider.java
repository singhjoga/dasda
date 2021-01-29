package com.harbois.komrade.runner.scm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.harbois.komrade.runner.Password;

public class SvnScmProvider extends ScmProvider {
	private static final Logger LOG = LoggerFactory.getLogger(SvnScmProvider.class);

	private SVNClientManager clientManager;
	private SVNRepository repository;

	public SvnScmProvider(String username, Password password, String baseUrl) {
		super(username, password, baseUrl);
	}

	public SvnScmProvider(String username, Password password) {
		super(username, password);
	}

	protected SVNClientManager getClientManager() {
		if (clientManager == null) {
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.getValue());
			ISVNOptions options = SVNWCUtil.createDefaultOptions(true);

			clientManager = SVNClientManager.newInstance(options, authManager);
		}
		return clientManager;
	}

	protected void copy(String sourceUrl, String destinationUrl) throws ScmException {
		SVNCopyClient copyClient = getClientManager().getCopyClient();

		try {
			SVNURL srcURL = SVNURL.parseURIEncoded(sourceUrl);
			SVNURL dstURL = SVNURL.parseURIEncoded(destinationUrl);
			SVNCopySource copySource = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcURL);

			copyClient.doCopy(new SVNCopySource[] { copySource }, dstURL, false, false, true, "Tag created by CICD Framework", null);
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	protected void commitWorkingDir(File workingDir) throws ScmException {
		SVNCommitClient client = getClientManager().getCommitClient();

		try {
			client.doCommit(new File[] { workingDir }, false, "Perform Release by CICD Framework", null, null, false, false, SVNDepth.FILES);
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	protected void deleteDir(String remoteUrl) throws ScmException {
		SVNCommitClient client = getClientManager().getCommitClient();

		try {
			SVNURL dstURL = SVNURL.parseURIEncoded(remoteUrl);
			client.doDelete(new SVNURL[] { dstURL }, "Deleted by CICD Frameowork");
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	protected void copyWorkingDir(File workingDir, String destinationUrl) throws ScmException {
		SVNCopyClient copyClient = getClientManager().getCopyClient();

		try {
			SVNURL dstURL = SVNURL.parseURIEncoded(destinationUrl);
			SVNCopySource copySource = new SVNCopySource(SVNRevision.WORKING, SVNRevision.WORKING, workingDir);

			copyClient.doCopy(new SVNCopySource[] { copySource }, dstURL, false, true, true, "Tag created by CICD Framework", null);
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	protected SVNRepository getRepository() throws ScmException {
		if (repository == null) {
			try {
				repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(baseUrl));
				ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.getValue());
				repository.setAuthenticationManager(authManager);
			} catch (SVNException e) {
				throw new ScmException(e.getMessage(), e);
			}
		}
		return repository;

	}

	protected void createDirIfNotExists(String dir) throws ScmException {
		SVNCommitClient commitClient = getClientManager().getCommitClient();
		commitClient.setIgnoreExternals(false);
		SVNNodeKind nodeKind = null;
		SVNRepository repo = getRepository();
		try {
			nodeKind = repo.checkPath(dir, -1);
			if (nodeKind == SVNNodeKind.NONE) {
				LOG.info(" Directory " + dir + " does not exist. Creating new.");
				String url = baseUrl + "/" + dir;
				commitClient.doMkDir(new SVNURL[] { SVNURL.parseURIEncoded(url) }, "Created folder");
			}
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	protected boolean dirExists(String dir) throws ScmException {
		SVNCommitClient commitClient = getClientManager().getCommitClient();
		commitClient.setIgnoreExternals(false);
		SVNNodeKind nodeKind = null;
		SVNRepository repo = getRepository();
		try {
			nodeKind = repo.checkPath(dir, -1);
			if (nodeKind == SVNNodeKind.NONE) {
				return false;
			} else {
				return true;
			}
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	@Override
	public void createTag(String sourceUrl, String tagName) throws ScmException {
		String destUrl = baseUrl.toString() + "/tags/" + tagName;
		LOG.info("Creating tag " + destUrl + " from " + sourceUrl);
		createDirIfNotExists("tags");
		copy(sourceUrl, destUrl);
	}

	@Override
	public void createBranchInternal(String sourceUrl, String branchName) throws ScmException {
		String destUrl = baseUrl.toString() + "/branches/" + branchName;
		LOG.info("Creating branch " + destUrl + " from " + sourceUrl);
		createDirIfNotExists("branches");
		copy(sourceUrl, destUrl);
	}

	@Override
	public void commit(File workingDir) throws ScmException {
		LOG.info("Committing " + workingDir.getAbsolutePath());
		commitWorkingDir(workingDir);
	}

	@Override
	public String getSourceUrlFromDir(File dir) throws ScmException {
		SVNWCClient client = SVNClientManager.newInstance().getWCClient();
		SVNInfo info;
		try {
			info = client.doInfo(dir, SVNRevision.WORKING);
			return info.getURL().toString();
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}

	}

	@Override
	public void createTag(File workingDir, String tagName) throws ScmException {
		String destUrl = baseUrl.toString() + "/tags/" + tagName;
		LOG.info("Creating tag " + destUrl + " from working dir " + workingDir.getAbsolutePath());
		createDirIfNotExists("tags");
		copyWorkingDir(workingDir, destUrl);
	}

	@Override
	public void deleteTag(String tagName) throws ScmException {
		String destUrl = baseUrl.toString() + "/tags/" + tagName;
		LOG.info("Delete tag " + destUrl);
		deleteDir(destUrl);
	}

	@Override
	public List<ScmEntry> getEntries(String url, boolean recursive) throws ScmException {
		List<ScmEntry> list = new ArrayList<>();
		SVNLogClient logClient = getClientManager().getLogClient();
		LOG.info("Getting SCM entries for " + url);
		try {

			logClient.doList(SVNURL.parseURIEncoded(url), SVNRevision.HEAD, SVNRevision.HEAD, false, SVNDepth.IMMEDIATES, SVNDirEntry.DIRENT_ALL,
					new ISVNDirEntryHandler() {
						public void handleDirEntry(SVNDirEntry entry)// throws SVNException
						{
							if (!StringUtils.isEmpty(entry.getName())) {
								list.add(svnEntry2ScmEntry(entry));
							}
						}
					});
			sortScmEntries(list);
			return list;
		} catch (SVNException e) {
			if (e.getMessage().contains("E160013")) {
				// Url not found. Return blank list
				return list;
			} else {
				throw new ScmException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void checkout(File destFolder) throws ScmException {
		checkout(baseUrl, destFolder);
	}

	@Override
	public void checkout(String url, File destFolder) throws ScmException {
		if (StringUtils.isEmpty(url)) {
			throw new ScmException("No url for checkout provided");
		}
		SVNUpdateClient client = getClientManager().getUpdateClient();
		try {
			LOG.info("Checking out "+url+" to "+destFolder.getAbsolutePath());
			client.doCheckout(SVNURL.parseURIEncoded(url), destFolder, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, true);
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	@Override
	public void export(File destFolder) throws ScmException {
		checkout(baseUrl, destFolder);
	}

	@Override
	public void export(String url, File destFolder) throws ScmException {
		if (StringUtils.isEmpty(url)) {
			throw new ScmException("No url for checkout provided");
		}
		SVNUpdateClient client = getClientManager().getUpdateClient();
		try {
			client.doExport(SVNURL.parseURIEncoded(url), destFolder, SVNRevision.HEAD, SVNRevision.HEAD, null, true, SVNDepth.INFINITY);
		} catch (SVNException e) {
			throw new ScmException(e.getMessage(), e);
		}
	}

	private ScmEntry svnEntry2ScmEntry(SVNDirEntry svnEntry) {
		ScmEntry scmEntry = new ScmEntry();
		scmEntry.setName(svnEntry.getName());
		scmEntry.setUrl(svnEntry.getURL().toString());
		scmEntry.setLastUpdateDate(svnEntry.getDate());
		scmEntry.setLastUpdateUser(svnEntry.getAuthor());
		scmEntry.setRevision(String.valueOf(svnEntry.getRevision()));

		return scmEntry;
	}

	@Override
	public boolean relativePathExists(String relativePath) throws ScmException {
		return dirExists(relativePath);
	}

	@Override
	public Map<String, List<ScmEntry>> getSourceTree(String url, boolean branches, boolean tags, boolean trunk) throws ScmException {

		Map<String, List<ScmEntry>> entryMap = new LinkedHashMap<>();

		if (trunk) {
			// Get the entries at the base url. If trunk exists, it should return
			List<ScmEntry> entries = getEntries(url, false);
			for (ScmEntry entry : entries) {
				if (entry.getName().equals("trunk")) {
					entryMap.put("trunk", Collections.singletonList(entry));
					break;
				}
			}
		}
		if (tags) {
			// Get the entries at the base url. If trunk exists, it should return
			List<ScmEntry> entries = getEntries(url + "/tags", false);
			entryMap.put("tags", entries);
		}
		if (branches) {
			// Get the entries at the base url. If trunk exists, it should return
			List<ScmEntry> entries = getEntries(url + "/branches", false);
			entryMap.put("branches", entries);
		}

		return entryMap;

	}

	@Override
	public void archive(File destFile) throws ScmException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void archive(String url, File destFile) throws ScmException {
		// TODO Auto-generated method stub
		
	}
}
