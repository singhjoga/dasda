package com.harbois.oauth.server.api.v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harbois.oauth.server.api.v1.common.Constants;
import com.harbois.oauth.server.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.server.api.v1.groups.Group;
import com.harbois.oauth.server.api.v1.groups.GroupService;
import com.harbois.oauth.server.security.Authorization;

@RestController
@RequestMapping("/api/v1/groups")
@Authorization(anyRoles=Constants.ROLE_CLIENT_ADMIN)
public class GroupController extends BaseResourceController<Group, Long> {
	private GroupService service;
	@Autowired
	public GroupController(GroupService service) {
		super(service);
		this.service=service;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/roles")
	public @ResponseBody ResponseEntity<?> getRoles(@PathVariable Long id) {
		Object body = RestResponseBuilder.getResponse(service.getRoleList(id));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/attachroles")
	public @ResponseBody ResponseEntity<?> attachRoles(@PathVariable Long id, @RequestBody Long[] roleIds) {
		service.attachRoles(id, roleIds);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/detachroles")
	public @ResponseBody ResponseEntity<?> detachRoles(@PathVariable Long id, @RequestBody Long[] roleIds) {
		service.detachRoles(id, roleIds);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/users")
	public @ResponseBody ResponseEntity<?> getUsers(@PathVariable Long id) {
		Object body = RestResponseBuilder.getResponse(service.getUserList(id));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/attachusers")
	public @ResponseBody ResponseEntity<?> attachUsers(@PathVariable Long id, @RequestBody String[] usernames) {
		service.attachUsers(id, usernames);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/detachusers")
	public @ResponseBody ResponseEntity<?> detachUsers(@PathVariable Long id, @RequestBody String[] usernames) {
		service.detachUsers(id, usernames);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
}
