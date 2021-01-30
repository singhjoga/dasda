package net.devoat.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
	public static void copyNonNullProperties(Object source, Object target, String... fieldNames) {
		copyNonNullProperties(source, target, false,fieldNames);
	}

	public static String copyNonNullProperties(Object source, Object target, boolean returnChanges, String... fieldNames) {
		if (source == null || target == null) {
			return null;
		}
		DiffBuilder<?> builder=null;
		if (returnChanges) {
			builder = new DiffBuilder<>(target, source, ToStringStyle.NO_CLASS_NAME_STYLE);
		}
		for (String filedName : fieldNames) {
			try {
				Object value = PropertyUtils.getProperty(source, filedName);
				if (returnChanges && value != null) {
					Object oldValue = PropertyUtils.getProperty(target, filedName);
					builder.append(filedName, oldValue,value);
				}
				if (value != null) {
					PropertyUtils.setProperty(target, filedName, value);
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
		}
		if (returnChanges) {
			String result = builder.build().toString();
			//replace the message
			result = StringUtils.replace(result, "differs from", "changed to");
			return result;
		}else {
			return null;
		}
	}
	
	public static String diff(Object oldObj, Object newObj, String... fieldNames) {
		DiffBuilder<?> builder = new DiffBuilder<>(oldObj, newObj, ToStringStyle.NO_CLASS_NAME_STYLE);
		for (String filedName : fieldNames) {
			try {
				Object oldValue = PropertyUtils.getProperty(oldObj, filedName);
				Object newValue = PropertyUtils.getProperty(newObj, filedName);
				builder.append(filedName, oldValue,newValue);

			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
		}
		String result = builder.build().toString();
		//replace the message
		result = StringUtils.replace(result, "differs from", "changed to");
		return result;
	}
	
	public static Method getMethod(Class<?> cls, String methodName) {
		try {
			Method method= cls.getDeclaredMethod(methodName);
			return method;
		} catch (NoSuchMethodException e) {
			for (Method method: cls.getDeclaredMethods()) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
			return null;
		} catch (SecurityException e) {
			throw new IllegalStateException(e.getMessage(),e);
		}
	}
}
