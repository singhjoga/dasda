package com.harbois.oauth.api.v1.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.oauth.api.v1.clients.OAuthClient;
import com.harbois.oauth.api.v1.clients.OAuthClientService;
import com.harbois.oauth.api.v1.common.Constants;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.controllers.dtos.Password;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/clients")
@Authorization(anyRoles=Constants.ROLE_CLIENT_ADMIN)
public class ClientsController extends BaseResourceController<OAuthClient, String> {
	private OAuthClientService service;
	@Autowired
	public ClientsController(OAuthClientService service) {
		super(service);
		this.service=service;
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	@Authorization(anyRoles=Constants.ROLE_SYS_ADMIN)
	public @ResponseBody ResponseEntity<?> add(@Valid @RequestBody OAuthClient obj) {
		return super.add(obj);
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/updatepassword")
	public @ResponseBody ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody Password passObj) {
		service.updatePassword(id, passObj.getPassword());
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
}
