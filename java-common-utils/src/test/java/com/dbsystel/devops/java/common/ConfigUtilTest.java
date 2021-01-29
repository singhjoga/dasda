package com.dbsystel.devops.java.common;

import java.util.Properties;

import org.junit.Test;

import com.thetechnovator.common.java.exceptions.BusinessException;
import com.thetechnovator.common.java.utils.ConfigUtil;

import junit.framework.Assert;

public class ConfigUtilTest {

	@Test
	public void testProperties() throws BusinessException {
		Properties props = new Properties();
		props.setProperty("repos.0.files.1.envcodes", "DEV");
		props.setProperty("repos.0.files.1.name", "xxxxx");
		props.setProperty("gitpassword", "ppppp");
		props.setProperty("repos.1.files.0.password", "pppp22");
		props.setProperty("repos.1.url", "http://xxxx");
		props.setProperty("repos.1.files.0.envcodes", "DEV,INT");
		props.setProperty("repos.0.url", "https://vvvvvv");
		props.setProperty("repos.0.files.0.envcodes", "PRD");
		props.setProperty("repos.1.files.0.name", "yyyyy");
		props.setProperty("repos.0.files.0.password", "p2p2p2p2p2");
		props.setProperty("repos.1.files.1.password", "p2p2p2p2");
		props.setProperty("gitusername", "uuuuuuuuuu");
		props.setProperty("repos.0.files.0.name", "zzzzzz");
		props.setProperty("repos.1.files.1.envcodes", "ABN,ABN2");
		props.setProperty("repos.1.files.1.name", "vvvvvvvv");
		props.setProperty("nos.0", "1");
		props.setProperty("nos.1", "2");
		props.setProperty("repos.1.tags.0", "tag1");
		props.setProperty("repos.1.tags.1", "tag2");
		KeePassConfig config = ConfigUtil.propertiesToBean(KeePassConfig.class, props);
		Assert.assertEquals("DEV", config.getRepos().get(0).getFiles().get(1).getEnvcodes());
		Assert.assertEquals("xxxxx", config.getRepos().get(0).getFiles().get(1).getName());
		Assert.assertEquals("ppppp", config.getGitPassword());
		Assert.assertEquals("pppp22", config.getRepos().get(1).getFiles().get(0).getPassword());
		Assert.assertEquals("http://xxxx", config.getRepos().get(1).getUrl());
		Assert.assertEquals("DEV,INT", config.getRepos().get(1).getFiles().get(0).getEnvcodes());
		Assert.assertEquals("https://vvvvvv", config.getRepos().get(0).getUrl());
		Assert.assertEquals("PRD", config.getRepos().get(0).getFiles().get(0).getEnvcodes());
		Assert.assertEquals("yyyyy", config.getRepos().get(1).getFiles().get(0).getName());
		Assert.assertEquals("p2p2p2p2p2", config.getRepos().get(0).getFiles().get(0).getPassword());
		Assert.assertEquals("p2p2p2p2", config.getRepos().get(1).getFiles().get(1).getPassword());
		Assert.assertEquals("uuuuuuuuuu", config.getGitUserName());
		Assert.assertEquals("zzzzzz", config.getRepos().get(0).getFiles().get(0).getName());
		Assert.assertEquals("ABN,ABN2", config.getRepos().get(1).getFiles().get(1).getEnvcodes());
		Assert.assertEquals("vvvvvvvv", config.getRepos().get(1).getFiles().get(1).getName());
		Assert.assertEquals(new Integer(1), config.getNos().get(0));
		Assert.assertEquals(new Integer(2), config.getNos().get(1));
		Assert.assertEquals("tag1", config.getRepos().get(1).getTags().get(0));
		Assert.assertEquals("tag2", config.getRepos().get(1).getTags().get(1));
		
	}
}
