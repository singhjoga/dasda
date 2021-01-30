package net.devoat.component.v1.version.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.devoat.common.auditing.AuditLog;
import net.devoat.common.exception.BadRequestException;
import net.devoat.common.services.BaseChildEntityService;

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
