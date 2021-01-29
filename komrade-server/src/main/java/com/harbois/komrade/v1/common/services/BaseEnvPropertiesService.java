package com.harbois.komrade.v1.common.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.common.auditing.AuditLogService;
import com.harbois.common.auditing.Auditable;
import com.harbois.common.auth.CrudActions;
import com.harbois.common.utils.ReflectUtil;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;
import com.harbois.komrade.v1.common.properties.SimpleProperty;
import com.harbois.komrade.v1.common.repositories.EnvPropertyRepository;
import com.harbois.komrade.v1.common.repositories.EnvPropertyValueRepository;
import com.harbois.oauth.api.v1.common.RawData;
import com.harbois.oauth.api.v1.common.domain.Deactivateable;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;
import com.harbois.oauth.api.v1.common.exception.ResourceNotFoundException;

@Component
public abstract class BaseEnvPropertiesService<T extends PersistentPropertyEnvBased<T,V>, V extends PersistentPropertyEnvBasedValue<V>> extends BaseEntityService<T, String>{
	@Autowired
	private AuditLogService auditService;
	private EnvPropertyRepository<T> repo;
	private EnvPropertyValueRepository<V> valueRepo;
	private Class<V> valueClass;
	@Autowired	
	public BaseEnvPropertiesService(EnvPropertyRepository<T> repo, Class<T> propClass, EnvPropertyValueRepository<V> valueRepo, Class<V> valueClass) {
		super(repo,propClass, String.class);
		this.repo=repo;
		this.valueRepo=valueRepo;
		this.valueClass=valueClass;
	}

	public T findByName(String propName) {
		return repo.findByName(propName);
	}

	@Override
	protected void beforeDelete(T savedObj) {
		List<V> values = valueRepo.findByPropertyId(savedObj.getId());
		if (values.size() != 0) {
			throw new BadRequestException("Global Variable cannot be deleted. " + values.size() + " values exist");
		}
		super.beforeDelete(savedObj);
	}
	private void assertNotDisabled(T obj) {
		if (obj instanceof Deactivateable) {
			if (Boolean.TRUE.equals(((Deactivateable)obj).getIsDisabled())) {
				throw new BadRequestException("Action cannot be performed. Property '" + obj.getName() + "' is disabled");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public String addValue(V value) {
		T prop = getById(value.getPropertyId());
		assertNotDisabled(prop);
		validateCredentialsEntry(prop, value.getEnvCode(), value.getValue());
		if (prop instanceof Auditable) {
			auditService.add(CrudActions.UPDATE, (Auditable<String>)prop, "Value set [" + value.getValue() + "]", value.getEnvCode());
		}
		setGeneratedId(value);
		valueRepo.save(value);
		return value.getId();
	}

	public V getValueById(String valueId, boolean throwErrorIfNotFound) {
		V value = valueRepo.findById(valueId).orElse(null);
		if (value == null && throwErrorIfNotFound) {
			throw new ResourceNotFoundException("GlobalVariableValue", valueId);
		}

		return value;
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public void updateValue(String valueId, V value) {
		V saved = getValueById(valueId, true);
		T prop = getById(saved.getPropertyId());
		assertNotDisabled(prop);
		String changes = ReflectUtil.copyNonNullProperties(value, saved, true, "envCode", "value");
		
		validateCredentialsEntry(prop, saved.getEnvCode(), saved.getValue());
		if (prop instanceof Auditable) {
			auditService.add(CrudActions.UPDATE, (Auditable)prop, changes, saved.getEnvCode());
		}
		valueRepo.save(saved);
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public void deleteValue(String valueId) {
		V value = getValueById(valueId, true);
		T prop = getById(value.getPropertyId());
		valueRepo.delete(value);
		if (prop instanceof Auditable) {
			auditService.add(CrudActions.UPDATE, (Auditable)prop, "Value deleted", value.getEnvCode());
		}
	}

	@Transactional
	public void updateFromRawData(String envCode, RawData rawData) {
		String line;
		Scanner scanner = new Scanner(rawData.getValue());
		List<String> errors = new ArrayList<>();
		List<SimpleProperty> props = new ArrayList<>();
		while (scanner.hasNext()) {
			line = scanner.next().trim();
			if (line.startsWith("#") || line.startsWith("-") || StringUtils.isEmpty(line)) {
				// comment/blank lines. Ignore them
				continue;
			}
			String name = StringUtils.substringBefore(line, "=");
			String value = StringUtils.substringAfter(line, "=");
			if (StringUtils.isEmpty(name)) {
				errors.add("Invalid line: " + line);
				continue;
			}

			SimpleProperty prop = new SimpleProperty(name,value);
			props.add(prop);
		}
		scanner.close();
		update(props,envCode);
	}
	public List<T> getAllWithValues(Set<String> envCodes) {
		List<T> props = repo.findAll();
		List<V> values = valueRepo.findByEnvCodeIn(envCodes);
		setValues(props, values);
		
		return props;
	}
	private V createValueObject() {
		try {
			return valueClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
	
	protected void setValues(List<T> props, List<V> allValues) {
		Map<String, T> map = props.stream().collect(Collectors.toMap(T::getId, v -> v));

		for (V value : allValues) {
			T prop = map.get(value.getPropertyId());
			if (prop == null)
				continue;
			List<V> propValues = prop.getValues();
			boolean valueFound = false;
			if (propValues == null) {
				propValues = new ArrayList<>();
				prop.setValues(propValues);
			} else {
				// check if value already exists. It can happen when the database queried
				// multiple times for the same property, then Hibernate returns the same Object
				for (V existingValue : propValues) {
					if (existingValue.getId().equals(value.getId())) {
						valueFound = true;
						break;
					}
				}
			}
			if (!valueFound) {
				propValues.add(value);
			}
		}
		// set blank list where there are no values
		for (T prop : props) {
			if (prop.getValues() == null) {
				prop.setValues(Collections.emptyList());
			}
		}
	}

	private void update(List<SimpleProperty> props, String envCode) {
		for (SimpleProperty prop: props) {
			T var = findByName(prop.getName());
			if (var == null) {
				throw new ResourceNotFoundException("GlobalVariable", prop.getName());
			}
			//get value
			V value = valueRepo.findByEnvCodeAndPropertyId(envCode, var.getId());
			if (value==null) {
				value = createValueObject();
				value.setEnvCode(envCode);
				value.setPropertyId(var.getId());
				value.setValue(prop.getValue());
				addValue(value);
			}else {
				value.setValue(prop.getValue());
				updateValue(value.getId(), value);
			}
		}
	}
	private void validateCredentialsEntry(T prop, String envCode, String value) {
		if (StringUtils.isEmpty(envCode) || StringUtils.isEmpty(value)) {
			// in case of update value, it is optional
			return;
		}
		//TODO:
		V valueObj = createValueObject();
		valueObj.setEnvCode(envCode);
		valueObj.setValue(value);
		//prop.setValues(Arrays.asList(valueObj));
		//propertyHelper.validateCredentialsEntriy(prop);
	}
}
