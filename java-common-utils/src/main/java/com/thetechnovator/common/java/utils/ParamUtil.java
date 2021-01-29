package com.thetechnovator.common.java.utils;

import org.apache.commons.lang3.StringUtils;

public class ParamUtil {
	public static void assertNotNull(Object param, String paramName) {
		if (param == null) {
			throw new IllegalArgumentException("Parameter "+paramName+" cannot be null");
		}
	}
	public static void assertNotEmpty(String param, String paramName) {
		if (StringUtils.isEmpty(param)) {
			throw new IllegalArgumentException("Parameter "+paramName+" cannot be empty");
		}
	}
	public static void assertGreaterThanZero(Number param, String paramName) {
		if (param == null) {
			throw new IllegalArgumentException("Parameter "+paramName+" cannot be null");
		}
		if (param.doubleValue() <= 0) {
			throw new IllegalArgumentException("Parameter "+paramName+" should be greater than zero");
		}
	}
}
