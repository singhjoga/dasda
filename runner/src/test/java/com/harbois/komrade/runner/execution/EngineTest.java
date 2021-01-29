package com.harbois.komrade.runner.execution;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.harbois.komrade.runner.context.Job;
import com.harbois.komrade.runner.exception.StepFailureException;
import com.harbois.komrade.runner.utils.CommonUtils;
import com.thetechnovator.common.java.StringProperties;
import com.thetechnovator.common.java.utils.FileUtil;

public class EngineTest {

	@Test
	public void testDoIt() throws IOException, StepFailureException {
		String fileContents = FileUtil.readFileAsStringFromClasspath("maven-build-docker.yaml");
		Engine engine = new Engine();
		Job job = new Job(CommonUtils.genUUID());
		job.setPipelineYaml(fileContents);
		File workingDir = new File(CommonUtils.getTempDir().getAbsolutePath()+File.separator+"engineTest2");
		if (workingDir.exists()) {
			//FileUtils.deleteDirectory(workingDir);
		}
		engine.doIt(job, new StringProperties(),workingDir);
	}
}
