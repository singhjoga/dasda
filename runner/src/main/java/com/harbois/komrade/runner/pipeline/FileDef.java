package com.harbois.komrade.runner.pipeline;

public class FileDef {
	private String name;
	private String targetFolder;
	private Boolean extract;
	
	public FileDef() {

	}
	public FileDef(String name, String targetFolder, Boolean extract) {
		super();
		this.name = name;
		this.targetFolder = targetFolder;
		this.extract = extract;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTargetFolder() {
		return targetFolder;
	}
	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}
	public boolean isExtract() {
		return extract;
	}
	public void setExtract(boolean extract) {
		this.extract = extract;
	}
	
}
