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
import com.harbois.oauth.server.api.v1.controllers.dtos.Password;
import com.harbois.oauth.server.api.v1.users.User;
import com.harbois.oauth.server.api.v1.users.UserService;
import com.harbois.oauth.server.security.Authorization;

@RestController
@RequestMapping("/api/v1/users")
@Authorization(anyRoles=Constants.ROLE_USER_MANAGER)
public class UserController extends BaseResourceController<User, String> {
	private UserService service;
	
	@Autowired
	public UserController(UserService service) {
		super(service);
		this.service=service;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/groups")
	@Authorization(anyRoles=Constants.ROLE_READER)
	public @ResponseBody ResponseEntity<?> getGroups(@PathVariable String id) {
		Object body = RestResponseBuilder.getResponse(service.getGroupList(id));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/attachtogroups")
	@Authorization(anyRoles=Constants.ROLE_CLIENT_ADMIN)
	public @ResponseBody ResponseEntity<?> attachToGroups(@PathVariable String id, @RequestBody Long[] groupIds) {
		service.attachToGroups(id, groupIds);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/detachfromgroups")
	@Authorization(anyRoles=Constants.ROLE_CLIENT_ADMIN)
	public @ResponseBody ResponseEntity<?> detachFromGroups(@PathVariable String id, @RequestBody Long[] groupIds) {
		service.detachFromGroups(id, groupIds);
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/{id}/updatepassword")
	public @ResponseBody ResponseEntity<?> setPassword(@PathVariable String id, @RequestBody Password passObj) {
		service.updatePassword(id, passObj.getPassword());
		Object body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}	
}
