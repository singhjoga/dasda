package com.harbois.komrade.v1.common.properties;

import java.util.List;

import com.thetechnovator.common.java.StringProperties;

public abstract class PropertyUtils {
	public static StringProperties asProperties(List<? extends PropertyWithValue<?>> propList) {
		StringProperties result = new StringProperties();
		
		for (PropertyWithValue<?> prop: propList) {
			result.put(prop.getName(), prop.getValue());
		}
		
		return result;
	}
}
