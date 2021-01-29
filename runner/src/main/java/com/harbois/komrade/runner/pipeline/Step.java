package com.harbois.komrade.runner.pipeline;

import java.util.ArrayList;
import java.util.List;

public class Step {
	private String name;
	private Ssh ssh;
	private List<String> scripts=new ArrayList<String>();
	private List<String> artifacts=new ArrayList<String>();
	private ContainerDef container;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Ssh getSsh() {
		return ssh;
	}
	public void setSsh(Ssh ssh) {
		this.ssh = ssh;
	}
	public List<String> getScripts() {
		return scripts;
	}
	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}
	public List<String> getArtifacts() {
		return artifacts;
	}
	public void setArtifacts(List<String> artifacts) {
		this.artifacts = artifacts;
	}
	public ContainerDef getContainer() {
		return container;
	}
	public void setContainer(ContainerDef container) {
		this.container = container;
	}
}