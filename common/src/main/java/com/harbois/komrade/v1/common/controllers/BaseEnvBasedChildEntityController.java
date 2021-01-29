package com.harbois.komrade.v1.common.controllers;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.v1.common.services.BaseEnvBasedChildEntityService;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.security.Authorization;

@Authorization
public abstract class BaseEnvBasedChildEntityController<T extends IdentifiableEntity<ID>, ID extends Serializable, PARENT_ID extends Serializable> extends BaseChildEntityController<T, ID, PARENT_ID> {
	private BaseEnvBasedChildEntityService<T, ID, PARENT_ID> service;
	public BaseEnvBasedChildEntityController(BaseEnvBasedChildEntityService<T, ID, PARENT_ID> service) {
		super(service);
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/forenv")
	@Authorization(action = CrudActions.VIEW)
	@JsonView(value = Views.List.class)
	public @ResponseBody ResponseEntity<ListRestResponse<T>> findAll(@RequestParam(value = "parentId", required = true) PARENT_ID parentId, //
			@RequestParam(value = "envCode", required = true) String[] envCodes) {
		Set<String> set = new TreeSet<>(Arrays.asList(envCodes));
		ListRestResponse<T> body = RestResponseBuilder.getResponse(service.findAll(parentId, set));
		return ResponseEntity.ok(body);
	}
}
