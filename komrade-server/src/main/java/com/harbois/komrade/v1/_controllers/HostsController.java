package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.resources.HostService;
import com.harbois.komrade.v1.resources.domain.Host;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/hosts")
@Authorization(entity=Entities.HOST)
public class HostsController extends BaseEntityController<Host, String> {
	@Autowired
	public HostsController(HostService service) {
		super(service);
	}
}
