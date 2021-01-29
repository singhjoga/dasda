package com.thetechnovator.common.java.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	public static Map<String, String> commaDelimitedKeyValueToMap(String str) {
		Map<String, String> map = new HashMap<String, String>();
		String[] kvArray = str.split(",");
		for (String kv : kvArray) {
			// Split based on first equal to sign, after that it is the value
			String key = StringUtils.substringBefore(kv, "=");
			String value = StringUtils.substringAfter(kv, "=");
			map.put(key, value);
		}

		return map;
	}

	public static List<String> splitIgnoreRoundBracket(String str) {
		List<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int brackets = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '(') {
				brackets++;
				sb.append(c);
			} else if (c == ')' && brackets > 0) {
				brackets--;
				sb.append(c);
			} else if (c == ',' && brackets == 0) {
				result.add(sb.toString());
				sb.delete(0, sb.length());
			} else {
				sb.append(c);
			}
		}
		if (sb.length() > 0) {
			result.add(sb.toString());
		}
		return result;
	}
	
	public static String removeAllWhitespaces(String inStr) {
		if (inStr==null) return null;
		String result = inStr.replaceAll("\\s+", "");
		
		return result;
	}
}
