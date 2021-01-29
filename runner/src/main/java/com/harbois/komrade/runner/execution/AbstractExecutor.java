package com.harbois.komrade.runner.execution;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harbois.komrade.runner.context.JobDataArchive;
import com.harbois.komrade.runner.context.JobDataFile;
import com.harbois.komrade.runner.context.PipelineContext;
import com.harbois.komrade.runner.exception.StepFailureException;
import com.harbois.komrade.runner.pipeline.Pipeline;
import com.harbois.komrade.runner.pipeline.Step;

public abstract class AbstractExecutor implements StepExecutor {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractExecutor.class);
	private static final String FILE_TIME = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";
	private static final String TIMESTAMP = "yyyyMMddHHmmssSSS";
	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP);
	private static final SimpleDateFormat fileTimeFormat = new SimpleDateFormat(FILE_TIME);
	private Pipeline pipeline;
	private Step step;
	private String workingDir;

	public AbstractExecutor(Pipeline pipeline, Step step) {
		this.pipeline = pipeline;
		this.step = step;
	}

	@Override
	public void execute() throws StepFailureException {
		try {
			connect(step);
			createWorkingDir();
			changeToWorkingDir(workingDir);
			// Copy data
			copyDataToTarget();
			for (String script : step.getScripts()) {
				internalExec(script, true);
			}
			copyDataFromTarget();
		} finally {
			disconnect();
		}

	}

	public String getWorkingDir() {
		return workingDir;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

	public PipelineContext getContext() {
		return PipelineContext.getInstance();
	}

	protected abstract void connect(Step step) throws StepFailureException;

	protected abstract void disconnect() throws StepFailureException;

	protected abstract void upload(File localFile, String remotePath) throws StepFailureException;

	protected abstract void download(String remotePath, File localFile) throws StepFailureException;

	protected abstract void exec(String cmd) throws StepFailureException;

	protected abstract void exec(String cmd, OutputStream out) throws StepFailureException;

	protected abstract void changeToWorkingDir(String workingDir) throws StepFailureException;

	private void copyDataFromTarget() throws StepFailureException {
		File archiveDir = getContext().getArtifactsDir();
		for (String artifactDef : step.getArtifacts()) {
			List<TargetFile> files = listFiles(artifactDef);
			for (TargetFile file : files) {
				String relativePath=file.getPath();
				String remotePath=file.getPath();
				if (isAbsolutePath(relativePath)) {
					if (!relativePath.startsWith(workingDir)) {
						throw new StepFailureException("Arhive path should always be relative to working directory");
					}
					relativePath = StringUtils.substringAfter(file.getPath(), workingDir);
				}else {
					remotePath=workingDir+"/"+relativePath;
				}
				File localFile = Paths.get(archiveDir.getAbsolutePath(), convertPathToLocalPath(relativePath)).toFile();
				LOG.info("Archiving " + file.getPath()+" to "+localFile.getAbsolutePath());
				download(remotePath, localFile);
			}
		}
	}
	private boolean isAbsolutePath(String path) {
		if (path.startsWith("\\")) {
			//windows abs path
			return true;
		}else if (path.startsWith("/")) {
			//x system abs path
			return true;
		} else if (path.length() > 3 && path.charAt(1)==':' && path.charAt(2)=='\\') {
				//windows drive path
				return true;
		}else {
			return false;
		}
	}
	private String convertPathToLocalPath(String path) {
		StringBuilder sb = new StringBuilder();
		char findChar = File.separatorChar == '/' ? '\\' : '/';
		for (int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);
			if (c == findChar) {
				c = File.separatorChar;
			}
			sb.append(c);
		}

		return sb.toString();
	}

	private void copyDataToTarget() throws StepFailureException {
		PipelineContext ctx = PipelineContext.getInstance();
		copyDataToTarget(workingDir, ctx.getJob().getData().getSourceFiles());
		copyDataToTarget(workingDir, ctx.getJob().getData().getConfigFiles());
	}

	private void copyDataToTarget(String destDir, List<JobDataFile> files) throws StepFailureException {
		for (JobDataFile file : files) {
			String dest = destDir;
			if (StringUtils.isNotEmpty(file.getDestDir())) {
				if (file.getDestDir().startsWith("/")) {
					// absolute path
					dest = file.getDestDir();
				} else {
					dest = destDir + "/" + file.getDestDir();
				}
			}
			// ensure the target path
			String cmd = "mkdir -p " + dest;
			internalExec(cmd, false);
			upload(file.getFile(), dest);
			if (file instanceof JobDataArchive) {
				JobDataArchive archive = (JobDataArchive) file;
				if (archive.isExtractOnDest()) {
					String remoteFile = dest + "/" + file.getFile().getName();
					extractFile(remoteFile, dest);
				}
			}
		}
	}

	public List<TargetFile> listFiles(String remotePathExpr) throws StepFailureException {

		String command = "ls -l --time-style=full-iso " + remotePathExpr;
		String output = internalExec(command, false);
		return parseTargetFiles(output);

	}

	private List<TargetFile> parseTargetFiles(String str) throws StepFailureException {
		/*
		 * Parse output in the below format -rw-r--r-- 1 twix16 twix16 10151867
		 * 2019-03-14 12:32:51.000000000 +0100
		 * /app/twix16/tibco/ae/tra/domain/DBE_AE_001/application/logs/asl-ndm-abrp-asl-
		 * ndm-abrp_1.log -rw-r--r-- 1 twix16 twix16 30715133 2018-11-13
		 * 11:41:22.000000000 +0100
		 * /app/twix16/tibco/ae/tra/domain/DBE_AE_001/application/logs/asl-ndm-abrp-asl-
		 * ndm-abrp_1.log.1
		 */
		List<TargetFile> list = new ArrayList<>();
		if (StringUtils.isEmpty(str)) {
			return list;
		}
		String[] lines = StringUtils.split(str, (char) 10);
		for (String line : lines) {
			LOG.debug("Parsing line: " + line);
			if (line.startsWith("total")) {
				continue;
			}
			String[] tokens = StringUtils.split(line, " ");
			if (tokens.length < 9) {
				throw new StepFailureException("Cannot parse remote list file output: " + line + ". Tokens: " + StringUtils.join(tokens, ","));
			}
			try {
				boolean isDir = tokens[0].charAt(0) == 'd';
				TargetFile file = new TargetFile(tokens[8], Long.parseLong(tokens[4]), fileTimeFormat.parse(tokens[5] + " " + tokens[6]), isDir);
				list.add(file);
			} catch (NumberFormatException | ParseException e) {
				throw new StepFailureException(e);
			}
		}

		return list;
	}

	private void extractFile(String remoteFilePath, String destDir) throws StepFailureException {
		String command = null;
		if (remoteFilePath.endsWith(".gz")) {
			command = "tar -xzf " + remoteFilePath + " -C " + destDir;
		} else if (remoteFilePath.endsWith(".tar")) {
			command = "tar -xf " + remoteFilePath + " -C " + destDir;
		} else if (remoteFilePath.endsWith(".zip")) {
			command = "unzip -q " + remoteFilePath + " -d " + destDir;
		}
		internalExec(command, false);
	}

	private String internalExec(String cmd, boolean showCmd) throws StepFailureException {
		if (!showCmd) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			exec(cmd, bos);
			return bos.toString();
		} else {
			LOG.info(cmd);
			exec(cmd);
			return null;
		}
	}

	private void createWorkingDir() throws StepFailureException {
		String dir="/tmp/komrade-"+timestampFormat.format(new Date());
		String cmd = "mkdir -p " + dir;
		internalExec(cmd, false);
		this.workingDir=dir;
	}
}
