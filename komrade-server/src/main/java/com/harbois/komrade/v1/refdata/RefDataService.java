package com.harbois.komrade.v1.refdata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harbois.komrade.v1.common.services.BaseEntityService;

@Component
public class RefDataService extends BaseEntityService<RefDataUser, String>{
	private static String USER_REF_DATA_TYPES="UserRefDataType";
	@Autowired	
	private RefDataSystemRepository systemRepo;
	private RefDataUserRepository repo;
	@Autowired	
	public RefDataService(RefDataUserRepository repo) {
		super(repo,RefDataUser.class, String.class);
		this.repo=repo;
	}
	public List<RefDataUser> findByUserReferenceType(String referenceType) {
		return repo.findByReferenceType(referenceType);
	}
	public List<RefDataSystem> findAllUserRefDataTypeNames() {
		return systemRepo.findByReferenceType(USER_REF_DATA_TYPES);
	}
	public List<RefDataSystem> findBySysReferenceType(String referenceType) {
		return systemRepo.findByReferenceType(referenceType);
	}
	public List<RefDataSystem> findAllSysReferenceType() {
		return systemRepo.findAll();
	}
}
