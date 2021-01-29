package com.harbois.komrade.runner.pipeline;

import java.util.Map;

public class PipelineDef {
	public static String[] TASK_TYPES= {"scm-checkout","scm-tag","scm-branch"};
	
	private Map<Object, Object> options;
	private Pipeline pipeline;
	public Map<Object, Object> getOptions() {
		return options;
	}
	public void setOptions(Map<Object, Object> options) {
		this.options = options;
	}
	public Pipeline getPipeline() {
		return pipeline;
	}
	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	
}
