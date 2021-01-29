package com.harbois.komrade.v1.component.version.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.harbois.common.auditing.AuditLog;
import com.harbois.komrade.v1.common.services.BaseChildEntityService;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;

@org.springframework.stereotype.Component
public class ComponentVersionActionService extends BaseChildEntityService<ComponentVersionAction, String,String>{
	@Autowired	
	public ComponentVersionActionService(ComponentVersionActionRepository repo) {
		super(repo,ComponentVersionAction.class, String.class);
	}

	@Override
	public void update(String id, ComponentVersionAction obj) {
		throw new BadRequestException("Operation not allowed");
	}

	@Override
	public void delete(String id) {
		throw new BadRequestException("Operation not allowed");
	}

	@Override
	public List<AuditLog> getHistory(String id) {
		throw new BadRequestException("Operation not allowed");
	}
	
}
