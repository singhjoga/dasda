package net.devoat.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean2;

public class NullAwareBeanUtilsBean extends BeanUtilsBean2{
	private Set<String> ignoreProperties;

	public NullAwareBeanUtilsBean(Set<String> ignoreProperties) {
		super();
		this.ignoreProperties = ignoreProperties;
	}
	public NullAwareBeanUtilsBean() {
		super();
		this.ignoreProperties = Collections.emptySet();
	}

	@Override
	public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
	    if(value==null)return;
	    if (ignoreProperties.contains(name)) return;
        super.copyProperty(bean, name, value);
	}

}
