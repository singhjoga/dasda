package com.thetechnovator.ssh;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class MiscTest {

	@Test
	public void testAbsolutePath() {
		
		Path p = Paths.get("/test");
		//Assert.assertTrue(p.isAbsolute());
		p = Paths.get("d:\\test");
		Assert.assertTrue(p.isAbsolute());
		p = Paths.get("\\test");
		Assert.assertTrue(p.isAbsolute());
		p = Paths.get("\\\\test");
		Assert.assertTrue(p.isAbsolute());
		p = Paths.get("test");
		Assert.assertFalse(p.isAbsolute());
	}
}
