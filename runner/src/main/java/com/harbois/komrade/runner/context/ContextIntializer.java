package com.harbois.komrade.runner.context;

import java.io.File;

public class ContextIntializer {
	public static void init(Job job, File workspaceDir) {
		PipelineContext ctx = PipelineContext.getInstance();
		ctx.setJob(job);
		ctx.setWorkspaceDir(workspaceDir);
	}
}
