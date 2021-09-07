package net.devoat.common.services;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import net.devoat.common.Constants;
import net.devoat.common.UserContext;
import net.devoat.common.auditing.AuditLog;
import net.devoat.common.auditing.AuditLogService;
import net.devoat.common.auditing.Auditable;
import net.devoat.common.auth.CrudActions;
import net.devoat.common.domain.BaseAuditableEntity;
import net.devoat.common.domain.ClientIdentity;
import net.devoat.common.domain.Deactivateable;
import net.devoat.common.domain.FunctionalId;
import net.devoat.common.domain.GeneratedUuid;
import net.devoat.common.domain.IdentifiableEntity;
import net.devoat.common.domain.StatusChangeable;
import net.devoat.common.exception.AccessDeniedException;
import net.devoat.common.exception.BadRequestException;
import net.devoat.common.exception.ResourceNotFoundException;
import net.devoat.common.repositories.BaseRepository;
import net.devoat.common.utils.EntityUtil;
import net.devoat.common.utils.ReflectUtil;
import net.devoat.common.validation.annotations.UpdatePolicy;

public abstract class BaseCrudService<T extends IdentifiableEntity<ID>, ID extends Serializable> extends BaseService{
	protected static final String STATUS_NEW = "N";
	protected static final String COMMA = ",";
	protected static final String STATUS_DEPRECATED = "D";
	private static final Logger LOG = LoggerFactory.getLogger(BaseCrudService.class);

	private BaseRepository<T, ID> repo;
	private Class<T> entityClass;
	@Autowired
	private AuditLogService auditService;
	
	public BaseCrudService(BaseRepository<T, ID> repo, Class<T> entityClass, Class<ID> idClass) {
		super();
		this.repo = repo;
		this.entityClass = entityClass;

	}

	public T getById(ID id) {
		T obj = repo.findById(id).orElse(null);
		if (obj == null) {
			throw new ResourceNotFoundException(entityClass.getSimpleName(), id.toString());
		}
		verifyAccess(obj,false);
		return obj;
	}

	public T findById(ID id) {
		T obj = repo.findById(id).orElse(null);
		verifyAccess(obj,false);

		return obj;
	}
	public List<AuditLog> getHistory(ID id) {
		T obj = getById(id);
		return auditService.find((Auditable<?>)obj);
	}
	@Transactional
	public T add(T obj) {
		if (obj instanceof BaseAuditableEntity) {
			setAuditData((BaseAuditableEntity) obj, true);
		}
		if (obj instanceof GeneratedUuid) {
			((GeneratedUuid)obj).setId(EntityUtil.genUUID());
		}else if (obj instanceof FunctionalId) {
			verifyUniqueId(obj, obj.getId());
		}
		if (obj instanceof Deactivateable) {
			Deactivateable deactivateable = (Deactivateable)obj;
			if (deactivateable.getIsDisabled()==null) {
				deactivateable.setIsDisabled(false);
			}
		}
		if (obj instanceof StatusChangeable) {
			StatusChangeable changeable = (StatusChangeable)obj;
			if (StringUtils.isEmpty(changeable.getStatus())) {
				changeable.setStatus(STATUS_NEW);
			}
		}
		beforeSave(obj, true);
		save(obj);
		afterSave(obj, true);
		if (!(obj instanceof BaseAuditableEntity) && (obj instanceof Auditable)) {
			//write the audit log, otherwise it is taken from the entity itself
			auditService.add(CrudActions.ADD, (Auditable<?>)obj, "Added");	
		}
		return obj;
	}
	protected void verifyUniqueId(T obj, ID id) {
		T saved = findById(id);
		if (saved !=null) {
			throw new BadRequestException("Functional id '"+obj.getId()+"' is already used");
		}
	}
	protected void save(T obj) {
		verifyAccess(obj,true);
		repo.save(obj);
	}
	protected void beforeSave(T newObj, boolean isAdd) {
		//entity specific login can come here
	}
	protected void afterSave(T savedObj, boolean isAdd) {
		//entity specific login can come here
	}
	protected void beforeDelete(T savedObj) {
		//entity specific login can come here
	}
	protected void afterDelete(T savedObj) {
		//entity specific login can come here
	}
	protected void setGeneratedId(Object obj) {
		if (obj instanceof GeneratedUuid) {
			((GeneratedUuid)obj).setId(EntityUtil.genUUID());
		}
	}
	@Transactional
	public void update(ID id, T obj) {
		T saved = getById(id);
		String changes = copyUpdatableFields(saved, obj);
		if (obj instanceof BaseAuditableEntity) {
			setAuditData((BaseAuditableEntity) saved, false);
		}
		beforeSave(saved, false);
		save(saved);
		if (saved instanceof Auditable) {
			auditService.add(CrudActions.UPDATE, (Auditable<?>)saved, changes);			
		}
		afterSave(saved, false);
	}
	@Transactional
	public void delete(ID id) {
		T saved = getById(id);
		verifyAccess(saved,true);
		beforeDelete(saved);
		repo.delete(saved);
		if (saved instanceof Auditable) {
			auditService.add(CrudActions.DELETE, (Auditable<?>)saved, "Deleted");			
		}
		afterDelete(saved);
	}

	public void verifyAccess(T obj, boolean isWriteAccess) {
		if (!(obj instanceof ClientIdentity)) {
			return;
		}
		String userClientId = getCurrentClientId();
		ClientIdentity clientIdObj = (ClientIdentity)obj;
		if (!isWriteAccess) {
			verifyReadAccess(clientIdObj, userClientId);
		}else {
			verifyWriteAccess(clientIdObj, userClientId);
		}
	}
	public void verifyReadAccess(ClientIdentity clientIdObj, String userClientId) {
		//read access is allowed for records belonging to the same clientId and also globals
		if (Constants.CLIENT_ID_GLOBAL.equals(clientIdObj.getClientId()) || userClientId.equals(clientIdObj.getClientId()) ) {
			return;
		}else {
			//user not allowed get this record
			LOG.error("Read access denied. Resource Client Id: "+clientIdObj.getClientId()+", User Client Id: "+userClientId);
			throw new AccessDeniedException("Read access denied!");
		}
	}
	public void verifyWriteAccess(ClientIdentity clientIdObj, String userClientId) {
		UserContext ctx = UserContext.getInstance();

		if  (Constants.CLIENT_ID_GLOBAL.equals(clientIdObj.getClientId())) {
			//only sysadmin can work on global client ids
			if (!ctx.isSystemAdmin()) {
				LOG.error("NonSysadmin tried to create global resource. User: "+ctx.getUsername()+". Resource Client Id: "+clientIdObj.getClientId()+", User Client Id: "+userClientId);
				throw new AccessDeniedException("Only SysAdmin can create/update global resources!");				
			}
		}
		if (ctx.isSystemAdmin() || (ctx.isClientAdmin() && userClientId.equals(clientIdObj.getClientId()))) {
			// updating its own record. User has to be Client Admin, or SysAdmin can do everything
			return;
		}else {
			//user not allowed get this record
			LOG.error("Write access denied. Resource Client Id: "+clientIdObj.getClientId()+", User Client Id: "+userClientId);
			throw new AccessDeniedException("Write access denied!");
		}
	}
	public List<T> filter(List<T> all) {
		Iterator<T> itr = all.iterator();
		if (!itr.hasNext()) {
			//no items
			return all;
		}
		//get the first item and check type
		T first = itr.next();
		if (!(first instanceof ClientIdentity)) {
			//no client filtering. return all
			return all;
		}
		//do the filtering for client
		String clientId = getCurrentClientId();
		List<T> result = new ArrayList<>();
		itr = all.iterator();
		while (itr.hasNext()) {
			T obj = itr.next();
			ClientIdentity clientIdObj = (ClientIdentity)obj;
			if (Constants.CLIENT_ID_GLOBAL.equals(clientIdObj.getClientId()) || clientId.equals(clientIdObj.getClientId()) ) {
				//only records with global or same clientId are returned
				result.add(obj);
			}
		}
		
		return result;
	}
	private String getCurrentClientId() {
		String clientId = UserContext.getInstance().getClientId();
		if (clientId == null) {
			throw new IllegalStateException("Client Id in UserContext cannot be null");
		}
		return clientId;
	}
	public String copyUpdatableFields(T saved, T obj) {
		DiffBuilder<?> builder = new DiffBuilder<>(saved, obj, ToStringStyle.NO_CLASS_NAME_STYLE);

		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value;
			try {
				value = PropertyUtils.getProperty(obj, field.getName());
				Boolean updateable = value != null;
				Annotation annotation = field.getAnnotation(UpdatePolicy.class);
				if (annotation != null) {
					updateable = (Boolean) ReflectUtil.getAnnotationFieldValue(annotation, "updateable");
					if (updateable && value == null) {
						//if field is updateable, check if it is always updateable
						Boolean alwaysUpdate = (Boolean) ReflectUtil.getAnnotationFieldValue(annotation, "alwaysUpdate");
						if (!alwaysUpdate) {
							updateable=false;
						}
					}
				}
				if (updateable) {
					Object oldValue = PropertyUtils.getProperty(saved, field.getName());
					builder.append(field.getName(), oldValue,value);
					PropertyUtils.setProperty(saved, field.getName(), value);
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
		}
		String result = builder.build().toString();
		//replace the message
		result = StringUtils.replace(result, "differs from", "changed to");
		return result;
	}

	public void setAuditData(BaseAuditableEntity obj, boolean isAdd) {
		if (isAdd) {
			obj.setAddDate(new Date());
			obj.setAddUser(getLoggedUser());
		} else {
			obj.setUpdateDate(new Date());
			obj.setUpdateUser(getLoggedUser());
		}
	}
}
