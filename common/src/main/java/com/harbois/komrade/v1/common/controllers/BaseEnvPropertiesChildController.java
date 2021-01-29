package com.harbois.komrade.v1.common.controllers;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;
import com.harbois.komrade.v1.common.services.BaseEnvPropertiesChildService;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.exception.BadRequestException;
import com.harbois.oauth.security.Authorization;

@Authorization()
public class BaseEnvPropertiesChildController<T extends PersistentPropertyEnvBased<T,V>, V extends PersistentPropertyEnvBasedValue<V>> extends BaseEnvPropertiesController<T, V> {
	private BaseEnvPropertiesChildService<T, V> service;
	@Autowired
	public BaseEnvPropertiesChildController(BaseEnvPropertiesChildService<T, V> service) {
		super(service);
		this.service=service;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/withvalues")
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class) 
	public @ResponseBody ResponseEntity<ListRestResponse<T>> findAllWithValue(
			@RequestParam(value = "parentId", required = true) String parentId, //
			@RequestParam(value = "envCode", required = true) String[] envCodes) {
		Set<String> set = new TreeSet<>(Arrays.asList(envCodes));
		ListRestResponse<T> body = RestResponseBuilder.getResponse(service.findAllWithValues(parentId, set));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.GET)
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class) 
	public @ResponseBody ResponseEntity<ListRestResponse<T>> findAll(
			@RequestParam(value = "parentId", required = true) String parentId) {
		ListRestResponse<T> body = RestResponseBuilder.getResponse(service.findAll(parentId));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.GET, value = "/notallowed")
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class) 
	@Override
	public ResponseEntity<ListRestResponse<T>> findAll() {
		throw new BadRequestException("This operation is not allowed on this entity");
	}
	
}
