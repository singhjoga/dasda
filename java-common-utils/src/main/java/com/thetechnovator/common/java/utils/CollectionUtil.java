package com.thetechnovator.common.java.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

public class CollectionUtil {
	
	public static String toDelimitedString(Object[] ary, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (Object obj: ary) {
			if (sb.length() > 0) {
				sb.append(delimiter);
			}
			sb.append(obj.toString());
		}
		
		return sb.toString();
	}
	public static String toDelimitedString(List<?> list, String delimiter) {
		if (list == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj: list) {
			if (sb.length() > 0) {
				sb.append(delimiter);
			}
			sb.append(obj.toString());
		}
		
		return sb.toString();
	}
	
	public static List<String> delimitedToStringList(String str, String delimiter) {
		String[] ary = str.split(delimiter);
		return Arrays.asList(ary);
	}
	public static List<Long> delimitedToLongList(String str, String delimiter) {
		String[] ary = str.split(delimiter);
		List<Long> list = new ArrayList<>();
		for (String obj: ary) {
			list.add(NumberUtils.createLong(obj));
		}
		return list;
	}
	
	public static <T> List<T> toList(Iterable<T> iterable) {
		List<T> list = new ArrayList<>();
		iterable.forEach(i -> list.add(i));
		
		return list;
	}
}
