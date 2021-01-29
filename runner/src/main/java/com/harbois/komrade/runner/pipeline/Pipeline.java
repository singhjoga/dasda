package com.harbois.komrade.runner.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.harbois.komrade.runner.utils.CommonUtils;
import com.thetechnovator.common.java.StringProperties;

public class Pipeline {
	private StringProperties parameters;
	private List<Scm> scm =new ArrayList<Scm>();
	private List<Step> steps;
	private List<FileDef> files;
	private List<ContainerDef> containers=new ArrayList<>();
	public List<FileDef> getFiles() {
		return CommonUtils.nonNullList(files);
	}
	public void setFiles(List<FileDef> files) {
		this.files = files;
	}
	public StringProperties getParameters() {
		return CommonUtils.nonNullStringProperties(parameters);
	}
	public void setParameters(StringProperties parameters) {
		this.parameters = parameters;
	}
	public List<Step> getSteps() {
		return CommonUtils.nonNullList(steps);
	}
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	public List<Scm> getScm() {
		return CommonUtils.nonNullList(scm);
	}
	public void setScm(List<Scm> scm) {
		this.scm = scm;
	}
	public List<ContainerDef> getContainers() {
		return CommonUtils.nonNullList(containers);
	}
	public void setContainers(List<ContainerDef> containers) {
		this.containers = containers;
	}
}