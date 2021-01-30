package net.devoat.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	
	public static <T> T getObject(String json, Class<T> cls) {
		try {
			return jsonMapper.readValue(json, cls);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
