package com.harbois.komrade.runner.execution;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.harbois.komrade.runner.exception.StepFailureException;
import com.harbois.komrade.runner.pipeline.Pipeline;
import com.thetechnovator.common.java.utils.FileUtil;

public class YamlTest {
	@Test
	public void testDoIt() throws IOException, StepFailureException {
		String fileContents = FileUtil.readFileAsStringFromClasspath("maven-build-docker.yaml");
		Yaml yaml = new Yaml();
		Map<String, Object> map= yaml.load(fileContents);
		String newYaml=yaml.dump(map.get("pipeline"));
		Pipeline pipeline = yaml.loadAs(newYaml, Pipeline.class);
		System.out.println(newYaml);
	}
}
