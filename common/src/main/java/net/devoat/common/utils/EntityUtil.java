package net.devoat.common.utils;

import java.util.UUID;

import org.springframework.util.StringUtils;

public class EntityUtil {
	public static String genUUID() {
		return StringUtils.replace(UUID.randomUUID().toString(),"-","");
	}
}
