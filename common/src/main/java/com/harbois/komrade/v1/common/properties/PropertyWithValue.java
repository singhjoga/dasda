package com.harbois.komrade.v1.common.properties;

public interface PropertyWithValue<T extends PropertyWithValue<T>> extends TypedProperty<T>{
	String getValue();
	void setValue(String value);
}
