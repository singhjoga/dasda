package com.thetechnovator.common.java;

import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class StringProperties extends TreeMap<String, String>{
	private static final long serialVersionUID = 1L;
	public Boolean getAsBoolean(String key) {
		String value = get(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		value = value.trim().toLowerCase();
		if (value.equals("true")) {
			return true;
		}else if (value.equals("false")) {
			return false;
		}else {
			throw new IllegalStateException("Value '"+value+"' is not a boolean value");
		}
	}
	public Integer getAsInteger(String key) {
		String value = get(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		value = value.trim();
		return Integer.valueOf(value);
	}
	public Long getAsLong(String key) {
		String value = get(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		value = value.trim();
		return Long.valueOf(value);
	}
	
}
