package com.harbois.komrade.runner.context;

public class Job {
	private Component component;
	private String id;
	private String pipelineYaml;
	private JobData data=new JobData();
	
	public Job(String id) {
		super();
		this.id = id;
	}
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPipelineYaml() {
		return pipelineYaml;
	}
	public void setPipelineYaml(String pipelineYaml) {
		this.pipelineYaml = pipelineYaml;
	}
	public JobData getData() {
		return data;
	}
	
}
