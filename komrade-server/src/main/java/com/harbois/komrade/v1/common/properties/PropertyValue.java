package com.harbois.komrade.v1.common.properties;

public interface PropertyValue<T extends PropertyValue<T>> {
	String getId();
	void setId(String id);
	String getPropertyId();
	void setPropertyId(String propertyId);
	String getEnvCode();
	void setEnvCode(String envCode);
	String getValue();
	void setValue(String value);
	T create();
	T copy();
	T copyTo(T copyTo);

}