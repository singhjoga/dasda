package net.devoat.common.controllers;

import java.io.Serializable;

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
import net.devoat.common.services.BaseChildEntityService;
import net.devoat.oauth.security.Authorization;

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
