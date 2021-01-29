package com.thetechnovator.common.java.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public class ReflectUtil {
	public static Object getAnnotationFieldValue(Annotation annotation, String fieldName) {
		try {
			Object value = annotation.annotationType().getMethod(fieldName).invoke(annotation);
			return value;
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		}
	}
}
