package com.harbois.komrade.v1.globalvars;

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
import com.harbois.common.auth.CrudActions;
import com.harbois.common.utils.ReflectUtil;
import com.harbois.komrade.v1.common.properties.SimpleProperty;
import com.harbois.komrade.v1.common.services.BaseEntityService;
import com.harbois.komrade.v1.globalvars.refs.GlobalVariableReference;
import com.harbois.komrade.v1.globalvars.refs.GlobalVariableReferenceService;
import com.harbois.oauth.api.v1.common.RawData;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;
import com.harbois.oauth.api.v1.common.exception.ResourceNotFoundException;

@Component
public class GlobalVariableService2 extends BaseEntityService<GlobalVariable, String>{
	@Autowired
	private AuditLogService auditService;
	@Autowired
	private GlobalVariableValueRepository valueRepo;
	@Autowired
	private GlobalVariableReferenceService refService;
	private GlobalVariableRepository repo;
	@Autowired	
	public GlobalVariableService2(GlobalVariableRepository repo) {
		super(repo,GlobalVariable.class, String.class);
		this.repo=repo;
	}

	public GlobalVariable findByName(String propName) {
		return repo.findByName(propName);
	}

	@Override
	protected void beforeSave(GlobalVariable newObj, boolean isAdd) {
		if (isAdd) {
			return;
		}
		if (newObj.getName() != null) {
			//name changed, if there are property references, name cannot be changed
			GlobalVariable saved = getById(newObj.getId());
			if (!saved.getName().equals(newObj.getName())) {
				int refs = referencesExists(newObj.getId());
				if (refs != 0) {
					throw new BadRequestException("Name cannot be changed. " + refs + " references exist");
				}
			}
		}
		super.beforeSave(newObj, isAdd);
	}

	@Override
	protected void beforeDelete(GlobalVariable savedObj) {
		List<GlobalVariableValue> values = valueRepo.findByPropertyId(savedObj.getId());
		if (values.size() != 0) {
			throw new BadRequestException("Global Variable cannot be deleted. " + values.size() + " values exist");
		}
		int refs = referencesExists(savedObj.getId());
		if (refs != 0) {
			throw new BadRequestException("Global Variable cannot be deleted. " + refs + " references exist");
		}
		super.beforeDelete(savedObj);
	}
	
	private int referencesExists(String id) {
		List<GlobalVariableReference> references = refService.find(id);
		return references.size();
	}
	private void assertNotDisabled(GlobalVariable obj) {
		if (Boolean.TRUE.equals(obj.getIsDisabled())) {
			throw new BadRequestException("Action cannot be performed. Property '" + obj.getName() + "' is disabled");
		}
	}
	
	@Transactional
	public String addValue(GlobalVariableValue value) {
		GlobalVariable prop = getById(value.getPropertyId());
		assertNotDisabled(prop);
		validateCredentialsEntry(prop, value.getEnvCode(), value.getValue());
		auditService.add(CrudActions.UPDATE, prop, "Value set [" + value.getValue() + "]", value.getEnvCode());
		setGeneratedId(value);
		valueRepo.save(value);
		return value.getId();
	}

	public GlobalVariableValue getValueById(String valueId, boolean throwErrorIfNotFound) {
		GlobalVariableValue value = valueRepo.findById(valueId).orElse(null);
		if (value == null && throwErrorIfNotFound) {
			throw new ResourceNotFoundException("GlobalVariableValue", valueId);
		}

		return value;
	}

	@Transactional
	public void updateValue(String valueId, GlobalVariableValue value) {
		GlobalVariableValue saved = getValueById(valueId, true);
		GlobalVariable prop = getById(saved.getPropertyId());
		assertNotDisabled(prop);
		String changes = ReflectUtil.copyNonNullProperties(value, saved, true, "envCode", "value");
		validateCredentialsEntry(prop, saved.getEnvCode(), saved.getValue());
		auditService.add(CrudActions.UPDATE, prop, changes, saved.getEnvCode());
		valueRepo.save(saved);
	}

	@Transactional
	public void deleteValue(String valueId) {
		GlobalVariableValue value = getValueById(valueId, true);
		GlobalVariable prop = getById(value.getPropertyId());
		valueRepo.delete(value);
		auditService.add(CrudActions.UPDATE, prop, "Value deleted", value.getEnvCode());
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
	public List<GlobalVariable> getAllWithValues(Set<String> envCodes) {
		List<GlobalVariable> props = repo.findAll(0);
		List<GlobalVariableValue> values = valueRepo.findByEnvCodeIn(envCodes);
		//List<GlobalVariableValue> values = valueRepo.findByEnvCodeIn(envCodes.toArray(new String[0]));
		setValues(props, values);
		
		return props;
	}
	private void setValues(List<GlobalVariable> props, List<GlobalVariableValue> allValues) {
		Map<String, GlobalVariable> map = props.stream().collect(Collectors.toMap(GlobalVariable::getId, v -> v));

		for (GlobalVariableValue value : allValues) {
			GlobalVariable prop = map.get(value.getPropertyId());
			if (prop == null)
				continue;
			List<GlobalVariableValue> propValues = prop.getValues();
			boolean valueFound = false;
			if (propValues == null) {
				propValues = new ArrayList<>();
				prop.setValues(propValues);
			} else {
				// check if value already exists. It can happen when the database queried
				// multiple times for the same property, then Hibernate returns the same Object
				for (GlobalVariableValue existingValue : propValues) {
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
		for (GlobalVariable prop : props) {
			if (prop.getValues() == null) {
				prop.setValues(Collections.emptyList());
			}
		}
	}

	private void update(List<SimpleProperty> props, String envCode) {
		for (SimpleProperty prop: props) {
			GlobalVariable var = findByName(prop.getName());
			if (var == null) {
				throw new ResourceNotFoundException("GlobalVariable", prop.getName());
			}
			//get value
			GlobalVariableValue value = valueRepo.findByEnvCodeAndPropertyId(envCode, var.getId());
			if (value==null) {
				value = new GlobalVariableValue();
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
	private void validateCredentialsEntry(GlobalVariable prop, String envCode, String value) {
		if (StringUtils.isEmpty(envCode) || StringUtils.isEmpty(value)) {
			// in case of update value, it is optional
			return;
		}
		//TODO:
		GlobalVariableValue valueObj = new GlobalVariableValue();
		valueObj.setEnvCode(envCode);
		valueObj.setValue(value);
		//prop.setValues(Arrays.asList(valueObj));
		//propertyHelper.validateCredentialsEntriy(prop);
	}
}
