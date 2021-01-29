package com.harbois.komrade.runner.execution;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.harbois.komrade.runner.Password;
import com.harbois.komrade.runner.context.ContextIntializer;
import com.harbois.komrade.runner.context.Job;
import com.harbois.komrade.runner.context.JobData;
import com.harbois.komrade.runner.context.JobDataArchive;
import com.harbois.komrade.runner.context.JobDataFile;
import com.harbois.komrade.runner.context.PipelineContext;
import com.harbois.komrade.runner.exception.StepFailureException;
import com.harbois.komrade.runner.pipeline.FileDef;
import com.harbois.komrade.runner.pipeline.Pipeline;
import com.harbois.komrade.runner.pipeline.PipelineDef;
import com.harbois.komrade.runner.pipeline.Scm;
import com.harbois.komrade.runner.pipeline.ScmRepository;
import com.harbois.komrade.runner.pipeline.Step;
import com.harbois.komrade.runner.scm.GitCommand;
import com.harbois.komrade.runner.scm.ScmException;
import com.harbois.komrade.runner.scm.ScmProvider;
import com.harbois.komrade.runner.scm.ScmProvider.ProviderType;
import com.thetechnovator.common.java.StringProperties;

public class Engine {
	private static final String CONFIG_CACHE_PATH_PROP="komrade.config.cache.path";
	private static final String CONFIG_CACHE_PATH="D:\\work\\baaz-ws\\runner\\src\\test\\resources";
	public Engine() {
		init();
	}

	public void init() {
		// Set engine environment
		System.setProperty(GitCommand.GIT_BIN_PATH_PROP, "D:/Git/bin/git.exe");
		System.setProperty(CONFIG_CACHE_PATH_PROP, CONFIG_CACHE_PATH);
	}

	public void doIt(Job job, StringProperties context, File workingDir) throws StepFailureException {
		ContextIntializer.init(job, workingDir);
		Yaml yaml = new Yaml();
		Map<String, Object> map= yaml.load(job.getPipelineYaml());
		String newYaml=yaml.dump(map.get("pipeline"));
		Pipeline pipeline = yaml.loadAs(newYaml, Pipeline.class);
		if (pipeline.getScm() != null) {
			downloadSource(pipeline);
		}
		attachConfigFiles(pipeline);
		for (Step step : pipeline.getSteps()) {
			if (step.getSsh() != null) {
				//SshExecutor executor = new SshExecutor(pipeline, step);
				//executor.execute();
			}else if (step.getContainer() != null) {
				DockerExecutor executor = new DockerExecutor(pipeline, step);
				executor.execute();
			}
		}
	}
	private void attachConfigFiles(Pipeline pipeline) throws StepFailureException{
		//TODO: Download files from Server
		File localPath=new File(System.getProperty(CONFIG_CACHE_PATH_PROP));
		if (!localPath.exists() && pipeline.getFiles().size() > 0) {
			throw new StepFailureException("Local file storage path on runner not found: "+localPath.getAbsolutePath());
		}
		PipelineContext ctx = PipelineContext.getInstance();
		JobData jobData = ctx.getJob().getData();
		for (FileDef fileDef: pipeline.getFiles()) {
			File localFile = new File(localPath.getAbsolutePath()+File.separator+fileDef.getName());
			if (!localFile.exists()) {
				//this should not happen
				throw new StepFailureException("File not found on runner: "+localFile.getAbsolutePath());
			}
			if (fileDef.isExtract()) {
				//it is an archive file
				jobData.getConfigFiles().add(new JobDataArchive(localFile, fileDef.getTargetFolder(), true));
			}else {
				jobData.getConfigFiles().add(new JobDataFile(localFile, fileDef.getTargetFolder()));
			}
		}
	}
	private void downloadSource(Pipeline pipeline) throws StepFailureException {
		for (Scm scm: pipeline.getScm()) {
			downloadSource(scm.getRepository());
		}
	}
	
	private void downloadSource(ScmRepository repo)  throws StepFailureException{
		PipelineContext ctx = PipelineContext.getInstance();
		
		if (StringUtils.isEmpty(repo.getProvider())) {
			// TODO get it from url
		}
		if (!ProviderType.isValidValue(repo.getProvider())) {
			throw new StepFailureException("Not a valid SCM provider: " + repo.getProvider() + ". Valid values are: " + ProviderType.toDelimitedString());
		}
		Password pwd = new Password(repo.getCredentials().getPassword());
		ProviderType type = ProviderType.valueOf(repo.getProvider());
		ScmProvider provider = ScmProvider.getInstance(type, repo.getCredentials().getUsername(), pwd);
		File source = PipelineContext.getInstance().getSourceDir();
		File zipFile = new File(source.getAbsolutePath() + File.separator + "repo-contents.tar.gz");
		try {
			String repoUrl = repo.getRepositoryUrl() + "/" + repo.getRef();
			//TODO: Use path to download only a particular folder
			//provider.archive(repoUrl, zipFile);
			ctx.getJob().getData().getSourceFiles().add(new JobDataArchive(zipFile, null, true));
		//} catch (ScmException e) {
		//	throw new StepFailureException(e);
		}finally {
			
		}
	}
}
