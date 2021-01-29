package com.harbois.komrade.v1.globalvars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEnvPropertiesService;
import com.harbois.komrade.v1.globalvars.refs.GlobalVariableReference;
import com.harbois.komrade.v1.globalvars.refs.GlobalVariableReferenceService;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;

@Component
public class GlobalVariableService extends BaseEnvPropertiesService<GlobalVariable, GlobalVariableValue>{
	@Autowired
	private GlobalVariableReferenceService refService;
	public GlobalVariableService(GlobalVariableRepository repo, GlobalVariableValueRepository valueRepo) {
		super(repo,GlobalVariable.class, valueRepo, GlobalVariableValue.class);
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
		super.beforeDelete(savedObj);
		int refs = referencesExists(savedObj.getId());
		if (refs != 0) {
			throw new BadRequestException("Global Variable cannot be deleted. " + refs + " references exist");
		}
	}
	
	private int referencesExists(String id) {
		List<GlobalVariableReference> references = refService.find(id);
		return references.size();
	}
}
