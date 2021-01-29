package com.harbois.komrade.v1.common.controllers;

import java.io.Serializable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.v1.common.services.BaseEntityService;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.security.Authorization;

@Authorization
public abstract class BaseEntityController<T extends IdentifiableEntity<ID>, ID extends Serializable> extends BaseCrudController<T, ID> {
	private BaseEntityService<T, ID> service;
	public BaseEntityController(BaseEntityService<T, ID> service) {
		super(service);
		this.service = service;
	}
	@RequestMapping(method = RequestMethod.GET)
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class)
	public @ResponseBody ResponseEntity<ListRestResponse<T>> findAll() {
		ListRestResponse<T> body = RestResponseBuilder.getResponse(service.findAll());
		return ResponseEntity.ok(body);
	}
}
