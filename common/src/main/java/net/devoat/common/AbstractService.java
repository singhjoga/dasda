package net.devoat.common;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.devoat.common.domain.AuditableEntity;
import net.devoat.common.domain.ClientIdentity;
import net.devoat.common.domain.IdentifiableEntity;
import net.devoat.common.exception.AccessDeniedException;
import net.devoat.common.exception.ResourceNotFoundException;
import net.devoat.common.validation.annotations.UpdatePolicy;
import net.devoat.oauth.utils.ReflectUtil;

public abstract class AbstractService<T extends IdentifiableEntity<ID>, ID extends Serializable> {
	protected static final String NEW_FLAG = "N";
	protected static final String COMMA = ",";
	protected static final String DELETED_FLAG = "D";
	private static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);

	private CrudRepository<T, ID> repo;
	private Class<T> entityClass;

	public AbstractService(CrudRepository<T, ID> repo, Class<T> entityClass, Class<ID> idClass) {
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

	public T add(T obj) {
		if (obj instanceof AuditableEntity) {
			setAuditData((AuditableEntity<?>) obj, true);
		}
		beforeSave(obj, true);
		save(obj);
		afterSave(obj, true);
		return obj;
	}
	public void save(T obj) {
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
	public void update(ID id, T obj) {
		beforeSave(obj, false);
		T saved = getById(id);
		copyUpdatableFields(saved, obj);
		if (obj instanceof AuditableEntity) {
			setAuditData((AuditableEntity<?>) saved, false);
		}
		save(saved);
		afterSave(saved, false);
	}
	public void delete(ID id) {
		T saved = getById(id);
		verifyAccess(saved,true);
		beforeDelete(saved);
		repo.delete(saved);
		afterDelete(saved);
	}
	public Iterable<T> findAll() {
		return filter(repo.findAll());
	}
	public Iterable<T> findAllAsList() {
		List<T> list = new ArrayList<T>();
		filter(repo.findAll()).forEach(a -> list.add(a));
		return list;
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
	public Iterable<T> filter(Iterable<T> all) {
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
	public void copyUpdatableFields(T saved, T obj) {
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
					PropertyUtils.setProperty(saved, field.getName(), value);
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public String getLoggedUser() {
		// Get logged in user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();

	}

	public void setAuditData(AuditableEntity<?> obj, boolean isAdd) {
		if (isAdd) {
			obj.setAddDate(new Date());
			obj.setAddUser(getLoggedUser());
		} else {
			obj.setUpdateDate(new Date());
			obj.setUpdateUser(getLoggedUser());
		}
	}

}
