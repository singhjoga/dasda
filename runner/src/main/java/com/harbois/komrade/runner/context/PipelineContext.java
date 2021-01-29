package com.harbois.komrade.runner.context;

import java.io.File;

public class PipelineContext {
	private Job job;
	private File workspaceDir;

	private static ThreadLocal<PipelineContext> instance = new ThreadLocal<PipelineContext>() {
		@Override
		protected PipelineContext initialValue() {
			return new PipelineContext();
		}
		
	};

	private PipelineContext() {
	
	}

	public static PipelineContext getInstance() {
		return instance.get();
	}
	public File getWorkspaceDir() {
		return workspaceDir;
	}

	void setWorkspaceDir(File workspaceDir) {
		this.workspaceDir = workspaceDir;
	}
	public File getSourceDir() {
		return new File(workspaceDir.getAbsolutePath()+File.separator+"source");
	}
	public File getArtifactsDir() {
		return new File(workspaceDir.getAbsolutePath()+File.separator+"artifacts");
	}
	public File getFilesDir() {
		return new File(workspaceDir.getAbsolutePath()+File.separator+"files");
	}
	public File getConfigDir() {
		return new File(workspaceDir.getAbsolutePath()+File.separator+"config");
	}
	public Job getJob() {
		return job;
	}

	void setJob(Job job) {
		this.job = job;
	}
	
}

