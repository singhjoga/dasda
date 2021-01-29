package com.harbois.komrade.runner.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.thetechnovator.common.java.StringProperties;

public class CommonUtils {
	public static final StringProperties EMPTY_STRING_PROPERTIES=new StringProperties();
	public static File getWorkingDir() {
		return new File(System.getProperty("user.dir"));
	}
	public static File getTempDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}
	public static File getUserHomeDir() {
		return new File(System.getProperty("user.home"));
	}
	public static File createTempDir() throws IOException{
		Path tempDir = Files.createTempDirectory("komrade");
		return tempDir.toFile();
	}
	public static URL toURL(String url) {
		try {
			URL result = new URL(url);
			return result;
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}
	public static String genUUID() {
		return StringUtils.replace(UUID.randomUUID().toString(),"-","");
	}
	public static <T> List<T> nonNullList(List<T> list) {
		return list==null?Collections.emptyList():list;
	}
	public static <T,V> Map<T,V> nonNullMap(Map<T,V> map) {
		return map==null?Collections.emptyMap():map;
	}
	public static StringProperties nonNullStringProperties(StringProperties props) {
		return props==null?EMPTY_STRING_PROPERTIES:props;
	}
}
