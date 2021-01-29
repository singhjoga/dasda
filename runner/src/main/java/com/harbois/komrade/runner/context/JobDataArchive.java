package com.harbois.komrade.runner.context;

import java.io.File;

public class JobDataArchive extends JobDataFile{
	boolean extractOnDest;

	public JobDataArchive(File file, String destDir,boolean extractOnDest) {
		super(file, destDir);
		this.extractOnDest=extractOnDest;
	}

	public boolean isExtractOnDest() {
		return extractOnDest;
	}
	
}
