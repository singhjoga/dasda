package com.harbois.komrade.runner.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.harbois.komrade.runner.utils.CommonUtils;
import com.thetechnovator.common.java.StringProperties;

public class ContainerDef {
	private String image;
	private String alias;
	private List<String> volumes=new ArrayList<String>();
	private List<String> mounts=new ArrayList<String>();
	private List<String> dependsOn=new ArrayList<String>();
	private Boolean privileged=false;
	private StringProperties variables=new StringProperties();
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public List<String> getVolumes() {
		return CommonUtils.nonNullList(volumes);
	}
	public void setVolumes(List<String> volumes) {
		this.volumes = volumes;
	}
	public List<String> getMounts() {
		return CommonUtils.nonNullList(mounts);
	}
	public void setMounts(List<String> mounts) {
		this.mounts = mounts;
	}
	public List<String> getDependsOn() {
		return CommonUtils.nonNullList(dependsOn);
	}
	public void setDependsOn(List<String> dependsOn) {
		this.dependsOn = dependsOn;
	}
	public Boolean getPrivileged() {
		return privileged;
	}
	public void setPrivileged(Boolean privileged) {
		this.privileged = privileged;
	}
	public StringProperties getVariables() {
		return CommonUtils.nonNullStringProperties(variables);
	}
	public void setVariables(StringProperties variables) {
		this.variables = variables;
	}
	
}
