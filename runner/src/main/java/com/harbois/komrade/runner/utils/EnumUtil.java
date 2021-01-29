package com.harbois.komrade.runner.utils;

import java.util.StringJoiner;

public class EnumUtil {
	public static <E extends Enum<E>> boolean isValidValue(Class<E> cls, String str) {
		for (E en: cls.getEnumConstants()) {
			if (en.name().equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public static <E extends Enum<E>> String toDelimitedString(Class<E> cls) {
		StringJoiner joiner = new StringJoiner(",");
		for (E en: cls.getEnumConstants()) {
			joiner.add(en.name());
		}
		return joiner.toString();
	}
}
