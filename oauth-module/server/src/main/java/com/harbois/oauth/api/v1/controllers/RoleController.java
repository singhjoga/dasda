package com.harbois.oauth.api.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.oauth.api.v1.common.Constants;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.roles.Role;
import com.harbois.oauth.api.v1.roles.RoleService;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/roles")
@Authorization(anyRoles=Constants.ROLE_CLIENT_ADMIN)
public class RoleController extends BaseResourceController<Role, Long> {
	private RoleService service;
	@Autowired
	public RoleController(RoleService service) {
		super(service);
		this.service=service;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/groups")
	public @ResponseBody ResponseEntity<?> getGroups(@PathVariable Long id) {
		Object body = RestResponseBuilder.getResponse(service.getGroupList(id));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/attachtogroups")
	public @ResponseBody ResponseEntity<?> attachToGroups(@PathVariable Long id, @RequestBody Long[] groupIds) {
		service.attachToGroups(id, groupIds);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/detachfromgroups")
	public @ResponseBody ResponseEntity<?> detachFromGroups(@PathVariable Long id, @RequestBody Long[] groupIds) {
		service.detachFromGroups(id, groupIds);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
}
