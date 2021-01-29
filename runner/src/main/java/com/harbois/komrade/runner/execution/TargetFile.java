package com.harbois.komrade.runner.execution;

import java.util.Date;

public class TargetFile {
	private String path;
	private long size;
	private Date modifiedTime;
	private boolean isDirectory;
	public TargetFile(String path, long size, Date modifiedTime,boolean isDirector) {
		super();
		this.path = path;
		this.size = size;
		this.modifiedTime = modifiedTime;
		this.isDirectory=isDirector;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public boolean isDirectory() {
		return isDirectory;
	}
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	@Override
	public String toString() {
		return "RemoteFile [absolutePath=" + path + ", size=" + size + ", modifiedTime=" + modifiedTime
				+ ", isDirectory=" + isDirectory + "]";
	}
	
}
