package com.harbois.komrade.v1._controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.system.properties.SystemProperty;
import com.harbois.komrade.v1.system.properties.SystemPropertyService;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.RestResponse.AddRestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.NoResultRestResponse;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/system/properties")
@Authorization(entity=Entities.SYSTEM_PROPERTIES)
public class SystemPropertiesController extends BaseEntityController<SystemProperty, String> {
	@Autowired
	public SystemPropertiesController(SystemPropertyService service) {
		super(service);
	}

	@Override
	public ResponseEntity<AddRestResponse> add(@JsonView(value= Views.Add.class) @Valid @RequestBody SystemProperty obj) {
		throw new BadRequestException("Operation not allowed");
	}

	@Override
	public ResponseEntity<NoResultRestResponse> delete(@PathVariable String id) {
		throw new BadRequestException("Operation not allowed");
	}

}
