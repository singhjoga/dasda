package com.harbois.komrade.v1.common.controllers;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.v1.common.services.BaseChildEntityService;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.security.Authorization;

@Authorization
public abstract class BaseChildEntityController<T extends IdentifiableEntity<ID>, ID extends Serializable, PARENT_ID extends Serializable> extends BaseCrudController<T, ID> {
	private BaseChildEntityService<T, ID, PARENT_ID> service;

	public BaseChildEntityController(BaseChildEntityService<T, ID, PARENT_ID> service) {
		super(service);
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET)
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class) 
	public @ResponseBody ResponseEntity<ListRestResponse<T>> findAll(
			@RequestParam(value = "parentId", required = true) PARENT_ID parentId) {

		ListRestResponse<T> body = RestResponseBuilder.getResponse(service.findAll(parentId));
		return ResponseEntity.ok(body);
	}
	
}
