package com.harbois.oauth.api.v1.controllers;

import java.io.Serializable;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harbois.oauth.api.v1.common.AbstractService;
import com.harbois.oauth.api.v1.common.Constants;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.UserContext;
import com.harbois.oauth.api.v1.common.domain.IdentifiableEntity;
import com.harbois.oauth.security.Authorization;

@Authorization
public abstract class BaseResourceController<T extends IdentifiableEntity<ID>, ID extends Serializable> extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(BaseResourceController.class);
	private AbstractService<T, ID> service;
	public BaseResourceController(AbstractService<T, ID> service) {
		super();
		this.service = service;
	}
	@RequestMapping(method = RequestMethod.GET)
	@Authorization(anyRoles=Constants.ROLE_READER)
	public @ResponseBody ResponseEntity<?> findAll(HttpServletRequest req) {
		Object body = RestResponseBuilder.getResponse(service.findAll());
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> add(@Valid @RequestBody T obj) {
		T saved = service.add(obj);
		URI resUrl = getCurrentURI(saved.getId());
		Object body = RestResponseBuilder.addResponse(saved.getId().toString(), resUrl.toString());
		
		return ResponseEntity.created(resUrl).body(body);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@Authorization(anyRoles=Constants.ROLE_READER)
	public @ResponseBody ResponseEntity<?> getOne(@PathVariable ID id) {
		if (!UserContext.getInstance().hasAnyRole(Constants.ALL_ROLES)) {
			throw new AccessDeniedException("Access Denied: No required role");	
		}
		Object body = RestResponseBuilder.getResponse(service.getById(id));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
	public @ResponseBody ResponseEntity<?> update(@PathVariable ID id, @Valid @RequestBody T obj) {
		obj.setId(id);
		service.update(id, obj);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public @ResponseBody ResponseEntity<?> delete(@PathVariable ID id) {
		service.delete(id);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	
}
