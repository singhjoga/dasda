package com.harbois.komrade.v1.system.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;

@Component
public class SystemFileService extends BaseEntityService<SystemFile, String>{
	@Autowired	
	public SystemFileService(SystemFileRepository repo) {
		super(repo,SystemFile.class, String.class);
	}

	@Override
	protected void beforeSave(SystemFile newObj, boolean isAdd) {
		if (newObj.getContents() != null) {
			newObj.setSize((long)newObj.getContents().length());
		}
		super.beforeSave(newObj, isAdd);
	}
	
}
