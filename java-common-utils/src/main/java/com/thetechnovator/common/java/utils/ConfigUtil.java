package com.thetechnovator.common.java.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.thetechnovator.common.java.exceptions.BusinessException;

public class ConfigUtil {
	public static <T> T propertiesToBean(Class<T> beanClass, Properties props) throws BusinessException {
		return propertiesToBean(beanClass, props, null);
	}

	public static <T> T propertiesToBean(Class<T> beanClass, Properties props, String prefix) throws BusinessException {
		try {
			T obj = beanClass.newInstance();
			for (Object objKey : props.keySet()) {
				String name = (String) objKey;
				Object value = props.get(name);
				if (prefix != null && prefix.length() > 0) {
					name = StringUtils.substringAfter(name, prefix);
				}
				setPropertyValue(obj, name, value);
			}
			return obj;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	private static void setPropertyValue(Object obj, String propertyName, Object value) throws BusinessException {
		try {
			String name = propertyName;
			int pointIndex = propertyName.indexOf(".");
			boolean isNested=false;
			if (pointIndex != -1) {
				isNested=true;
				name = propertyName.substring(0, pointIndex);
				propertyName = propertyName.substring(pointIndex + 1);
			}
			Field field = findField(obj, name);
			if (field == null) {
				throw new BusinessException("Field "+name+" not defined in "+obj.getClass().getName());
			}
			field.setAccessible(true);
			Class<?> propType = field.getType();
			boolean isPrimitiveOrWrapperType = isBasicType(propType);
			
			//If it is primitive, it cannot be nested
			if (isPrimitiveOrWrapperType && isNested) {
				throw new BusinessException("Field "+name+" is a primitive or wrapper type, it cannot be nested");
			}
				
			boolean isList=List.class.isAssignableFrom(propType);
			Object propInstance = FieldUtils.readField(field, obj); 
			if (propInstance == null) {
				if (isList) {
					//varaible is of type List
					propInstance = new ArrayList<>();
				}else if (!isPrimitiveOrWrapperType){
					//simple object
					propInstance = propType.newInstance();
				}
				setFieldValue(obj, field, propInstance);
			}
			if (isList) {
					//varaible is of type List. Get the index
					pointIndex = propertyName.indexOf(".");
					String indexStr;
					if (pointIndex == -1) {
						if (propertyName.trim().length()==0) {
							throw new BusinessException("List property must be defined with indexes separated by dot '.'");							
						}
						indexStr = propertyName;
					}else {
						indexStr = propertyName.substring(0, pointIndex);
						propertyName = propertyName.substring(pointIndex + 1);
					}
					Integer index;
					try{
					    index = Integer.parseInt(indexStr);
					}catch (NumberFormatException e) {
						throw new BusinessException("List property does not have index value.");
					}
			        ParameterizedType elementType = (ParameterizedType) field.getGenericType();
			        Class<?> elementClass = (Class<?>) elementType.getActualTypeArguments()[0];
			        List<Object> list = (List<Object>)propInstance;
					ensureListSize(list,elementClass,index+1);
					//if the list element type is primitive or wrapper, there cannot be further nesting
					if (isBasicType(elementClass)) {
						Object listCalue = ConvertUtils.convert(value, elementClass);
						list.set(index, listCalue);
					}else {
						Object childObj = list.get(index);
						setPropertyValue(childObj, propertyName, value);
					}
			} else {
				if (isNested) {
					setPropertyValue(propInstance, propertyName, value);
				}else {
					setFieldValue(obj, field, value);
				}
			}
		} catch (IllegalAccessException e) {
			throw new BusinessException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new BusinessException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}
	private static Field findField(Object obj, String name) {
		for (Field field : FieldUtils.getAllFields(obj.getClass())) {
			if (field.getName().equalsIgnoreCase(name)) {
				return field;
			}
		}
		return null;
	}
	private static void setFieldValue(Object obj, Field field, Object value) throws BusinessException{
		if (value instanceof String) {
			value = ConvertUtils.convert(value, field.getType());
		}
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException e) {
			throw new BusinessException(e);
		} catch (IllegalAccessException e) {
			throw new BusinessException(e);
		}

	}
	private static void ensureListSize(List<Object> list, Class<?> type, int size) throws BusinessException {
		try {
			while (list.size() < size) {
				if (ClassUtils.isPrimitiveOrWrapper(type)) {
					list.add(null);					
				}else {
					Object obj = type.newInstance();
					list.add(obj);
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	private static boolean isBasicType(Class<?> type) {
		return ClassUtils.isPrimitiveOrWrapper(type) || String.class.equals(type);
	}
}
