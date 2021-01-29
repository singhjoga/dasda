package com.harbois.komrade.runner.context;

import java.util.ArrayList;
import java.util.List;

public class JobData {
	private List<JobDataFile> sourceFiles=new ArrayList<>();
	private List<JobDataFile> configFiles=new ArrayList<>();

	public List<JobDataFile> getSourceFiles() {
		return sourceFiles;
	}

	public List<JobDataFile> getConfigFiles() {
		return configFiles;
	}
	
}
