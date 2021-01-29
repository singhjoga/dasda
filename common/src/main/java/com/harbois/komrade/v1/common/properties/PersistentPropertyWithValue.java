package com.harbois.komrade.v1.common.properties;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistentPropertyWithValue<T extends PersistentPropertyWithValue<T>> extends PersistentProperty<T> implements PropertyWithValue<T>{
	@Column(name="VALUE")
	private String value;
	@Override
	public String getValue() {
		return value;
	}
	@Override
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public T copyTo(T copyTo) {
		super.copyTo(copyTo);
		copyTo.setValue(getValue());
		return copyTo;
	}
}