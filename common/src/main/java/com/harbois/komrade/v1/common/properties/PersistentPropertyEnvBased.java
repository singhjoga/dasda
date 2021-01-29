package com.harbois.komrade.v1.common.properties;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.oauth.api.v1.common.Views;

@MappedSuperclass
public abstract class PersistentPropertyEnvBased<T extends PersistentPropertyEnvBased<T,V>, V extends PersistentPropertyEnvBasedValue<V>> extends PersistentProperty<T>{
	@Transient
	@JsonView(value= {Views.List.class})
	private List<V> values=new ArrayList<>();

	public List<V> getValues() {
		return values;
	}
	public void setValues(List<V> values) {
		this.values = values;
	}
}
