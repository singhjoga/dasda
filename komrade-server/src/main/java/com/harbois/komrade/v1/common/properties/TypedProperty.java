package com.harbois.komrade.v1.common.properties;

public interface TypedProperty<T extends TypedProperty<T>> {
	String getName();
	void setName(String name);
	String getTypeCode();
	void setTypeCode(String typeCode);
}