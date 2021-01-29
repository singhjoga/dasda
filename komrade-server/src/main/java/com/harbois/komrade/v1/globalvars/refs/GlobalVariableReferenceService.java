package com.harbois.komrade.v1.globalvars.refs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harbois.komrade.v1.common.services.BaseService;
import com.harbois.komrade.v1.globalvars.GlobalVariable;
import com.harbois.komrade.v1.globalvars.GlobalVariableService;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;

@Service
public class GlobalVariableReferenceService extends BaseService{
	private static final Logger LOG = LoggerFactory.getLogger(GlobalVariableReferenceService.class);
	@Autowired
	private GlobalVariableReferenceRepository repo;
	@Autowired
	private GlobalVariableReferenceViewRepository viewRepo;
	@Autowired
	private GlobalVariableService propService;
	
	public void update(GlobalVariableReferenceable newValue, GlobalVariableReferenceable oldValue) {
		if (newValue != null && oldValue != null) {
			//it is an update. Check if the value is updated and then do the things
			if (StringUtils.equals(newValue.getValue(), oldValue.getValue())) {
				return;
			}
			//value change, delete previous references and add new
			delete(oldValue);
			add(newValue);
		}else if (newValue != null) {
			//add
			add(newValue);
		}else if (oldValue != null) {
			//delete
			delete(oldValue);
		}
	}
	public void add(GlobalVariableReferenceable newValue) {
		List<GlobalVariableReference> references= getReferences(newValue);
		if (references.isEmpty()) {
			return;
		}
		for (GlobalVariableReference ref: references) {
			//check if the reference is already there
			GlobalVariableReferenceId id= new GlobalVariableReferenceId(ref.getEntityType(), ref.getEntityId());
			if (repo.existsById(id)) {
				continue;
			}
			repo.save(ref);
		}
	}
	public List<GlobalVariableReference> getReferences(GlobalVariableReferenceable newValue) {
		
		if (newValue==null || StringUtils.isEmpty(newValue.getValue())) {
			return Collections.emptyList();
		}
		String value = newValue.getValue();
		String[] propNames = StringUtils.substringsBetween(value, "${", "}");
		if (propNames==null || propNames.length==0) {
			return Collections.emptyList();
		}
		List<GlobalVariableReference> result = new ArrayList<>();
		for (String propName: propNames) {
			LOG.info(propName);
			GlobalVariable prop = propService.findByName(propName);
			if (prop == null) {
				throw new BadRequestException("Global variable '"+propName+"' not found");
			}
			GlobalVariableReference obj = new GlobalVariableReference();

			obj.setEntityType(newValue.getEntityType());
			obj.setEntityId(newValue.getId());
			obj.setPropertyId(prop.getId());
			
			result.add(obj);
		}
		
		return result;
	}
	
	public void delete(GlobalVariableReferenceable oldValue) {
		if (oldValue == null || StringUtils.isEmpty(oldValue.getValue())) {
			return;
		}
		String value = oldValue.getValue();
		String[] propNames = StringUtils.substringsBetween(value, "${", "}");
		if (propNames==null || propNames.length==0) {
			return;
		}
			//check if the reference is already there
		GlobalVariableReferenceId id= new GlobalVariableReferenceId(oldValue.getEntityType(), oldValue.getId());
		if (!repo.existsById(id)) {
			//it is an error, but no point throwing an exception
			return;
		}
			
		repo.deleteById(id);
	}
	public List<GlobalVariableReference> find(String propertyId) {

		return repo.findByPropertyId(propertyId);
	}
	
	public List<GlobalVariableReferenceView> findView(String propertyId) {

		return viewRepo.findByPropertyId(propertyId);
	}
}
