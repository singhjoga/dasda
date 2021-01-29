package com.harbois.komrade.auth.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class EntityPermissions extends TreeMap<String, EntityPermission>{
	private static final long serialVersionUID = 1L;
	public List<EntityPermission> getPermissions() {
		return new ArrayList<EntityPermission>(values());
	}
}
