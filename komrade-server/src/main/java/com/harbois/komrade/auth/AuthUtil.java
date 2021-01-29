package com.harbois.komrade.auth;

import java.util.Map;

import com.harbois.komrade.auth.domain.EntityAction;
import com.harbois.komrade.auth.domain.EntityPermission;

public class AuthUtil {
	public static void merge(EntityPermission src, EntityPermission dest) {
		Map<String, EntityAction> srcActions = src.getActionsMap();
		Map<String, EntityAction> destActions = dest.getActionsMap();
		if (srcActions==null || srcActions.isEmpty()) {
			return;
		}
		for (String srcEntityName: srcActions.keySet()) {
			EntityAction srcEntity = srcActions.get(srcEntityName);
			EntityAction destEntity = destActions.get(srcEntityName);
			if (destEntity == null) {
				//not found in dest, add
				destActions.put(srcEntityName, srcEntity);
			}else {
				merge(srcEntity, destEntity);
			}
		}
	}
	public static void merge(EntityAction src, EntityAction dest) {
		if (src.getConstraintObj() ==null || src.getConstraintObj().isEmpty()) {
			return;
		}
		dest.getConstraintObj().putAll(src.getConstraintObj());
	}

}
