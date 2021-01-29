package com.harbois.komrade.v1.common.controllers;

import java.io.Serializable;
import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auditing.AuditLog;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.v1.common.services.BaseCrudService;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.AddRestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.NoResultRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.security.Authorization;
@Authorization
public class BaseCrudController<T extends IdentifiableEntity<ID>, ID extends Serializable> extends BaseController {
	private BaseCrudService<T, ID> service;
	public BaseCrudController(BaseCrudService<T, ID> service) {
		super();
		this.service = service;
	}
	@RequestMapping(method = RequestMethod.POST)
	@Authorization(action = CrudActions.ADD)
	@ResponseBody
	public ResponseEntity<AddRestResponse> add(@JsonView(value= Views.Add.class) @Valid @RequestBody T obj) {
		T saved = service.add(obj);
		URI resUrl = getCurrentURI(saved.getId());
		AddRestResponse body = RestResponseBuilder.addResponse(saved.getId().toString(), resUrl.toString());
		
		return ResponseEntity.created(resUrl).body(body);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@Authorization(action = CrudActions.VIEW)
	@JsonView(value = Views.View.class)
	@ResponseBody
	public ResponseEntity<RestResponse<T>> getOne(@PathVariable ID id) {
		RestResponse<T> body = RestResponseBuilder.getResponse(service.getById(id));
		return ResponseEntity.ok(body);
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	@Authorization(action = CrudActions.UPDATE)
	@ResponseBody
	public ResponseEntity<NoResultRestResponse> update(@PathVariable ID id, @JsonView(value= Views.Update.class) @Valid @RequestBody T obj) {
		obj.setId(id);
		service.update(id, obj);
		NoResultRestResponse body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@Authorization(action = CrudActions.DELETE)
	@ResponseBody
	public ResponseEntity<NoResultRestResponse> delete(@PathVariable ID id) {
		service.delete(id);
		NoResultRestResponse body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/history")
	@Authorization(action = CrudActions.VIEW)
	@JsonView(value = Views.View.class)
	@ResponseBody
	public ResponseEntity<ListRestResponse<AuditLog>> getHistory(@PathVariable ID id) {
		ListRestResponse<AuditLog> body = RestResponseBuilder.getResponse(service.getHistory(id));
		return ResponseEntity.ok(body);
	}

}