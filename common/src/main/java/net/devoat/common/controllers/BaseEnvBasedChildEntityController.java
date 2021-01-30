package net.devoat.common.controllers;

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

import net.devoat.common.RestResponseBuilder;
import net.devoat.common.Views;
import net.devoat.common.RestResponse.ListRestResponse;
import net.devoat.common.auth.CrudActions;
import net.devoat.common.domain.IdentifiableEntity;
import net.devoat.common.services.BaseEnvBasedChildEntityService;
import net.devoat.oauth.security.Authorization;

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
