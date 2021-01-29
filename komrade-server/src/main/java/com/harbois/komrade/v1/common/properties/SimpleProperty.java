package com.harbois.komrade.v1.common.properties;

public class SimpleProperty implements PropertyWithValue<SimpleProperty>{
	private String name;
	private String value;
	private String typeCode;
	public SimpleProperty(String name, String value) {
		this(name,value,null);
	}
	public SimpleProperty(String name, String value, String typeCode) {
		super();
		this.name = name;
		this.value = value;
		this.typeCode = typeCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
}
