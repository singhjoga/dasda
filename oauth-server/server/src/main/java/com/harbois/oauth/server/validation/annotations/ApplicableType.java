package com.harbois.oauth.server.validation.annotations;

public enum ApplicableType {
	OnlyOnInsert,//
	OnlyOnUpdate, //
	Both;
	
	public boolean onInsert() {
		return this == ApplicableType.OnlyOnInsert || this == ApplicableType.Both;
	}
	public boolean onUpdate() {
		return this == ApplicableType.OnlyOnUpdate|| this == ApplicableType.Both;
	}
}
