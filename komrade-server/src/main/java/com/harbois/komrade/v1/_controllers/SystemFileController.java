package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.system.files.SystemFile;
import com.harbois.komrade.v1.system.files.SystemFileService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/system/files")
@Authorization(entity=Entities.SYSTEM_FILE)
public class SystemFileController extends BaseEntityController<SystemFile, String> {
	@Autowired
	public SystemFileController(SystemFileService service) {
		super(service);
	}
}
