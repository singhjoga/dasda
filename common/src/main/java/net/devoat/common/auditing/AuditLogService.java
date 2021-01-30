package net.devoat.common.auditing;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.devoat.common.auth.CrudActions;
import net.devoat.common.domain.BaseAuditableEntity;
import net.devoat.common.services.BaseService;
import net.devoat.common.utils.EntityUtil;

@Service
public class AuditLogService extends BaseService{
	@Autowired
	private AuditLogRepository repo;
	public String add(String action, Auditable<?> auditable, String details) {
		return add(action,auditable, details, null);
	}
	public String add(String action, Auditable<?> auditable, String details, String filterValue) {
		if (StringUtils.isEmpty(details)) {
			return null;
		}
		if (filterValue != null) {
			details = filterValue+": "+details;
		}
	
		AuditLog obj = new AuditLog();
		obj.setAction(action);
		obj.setDate(new Date());
		obj.setDetails(details);
		obj.setObjectType(auditable.getObjectType());
		obj.setObjectId(auditable.getId().toString());
		obj.setObjectName(auditable.getName());
		obj.setUser(getLoggedUser());
		obj.setFilterValue(filterValue);
		obj.setId(EntityUtil.genUUID());
		repo.save(obj);
		
		return obj.getId();
	}
	
	public List<AuditLog> find(String objectType, String objectId) {
		return repo.findByObjectTypeAndObjectIdOrderByDateDesc(objectType, objectId);
	}
	public List<AuditLog> find(Auditable<?> auditable) {
		List<AuditLog> history = repo.findByObjectTypeAndObjectIdOrderByDateDesc(auditable.getObjectType(), auditable.getId().toString());
		//add the added record, which is not part of the history to preserve space
		AuditLog add = new AuditLog();
		if (auditable instanceof BaseAuditableEntity) { //this the case always
			BaseAuditableEntity common = (BaseAuditableEntity) auditable;
			if (common.getAddDate()==null) {
				return history;
			}
			add.setDate(common.getAddDate());
			add.setUser(common.getAddUser());
			add.setAction(CrudActions.ADD);
			add.setDetails("Added");
			add.setObjectId(auditable.getId().toString());
			add.setObjectName(auditable.getName());
			add.setObjectType(auditable.getObjectType());
			history.add(add);
		}
		return history; 
	}
}
