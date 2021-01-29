package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseCrudController;
import com.harbois.komrade.v1.system.external.ExternalSystem;
import com.harbois.komrade.v1.system.external.ExternalSystemService;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/external-systems")
@Authorization(entity=Entities.EXTERNAL_SYSTEM)
public class ExternalSystemController extends BaseCrudController<ExternalSystem, String> {
	private ExternalSystemService service;
	
	@Autowired
	public ExternalSystemController(ExternalSystemService service) {
		super(service);
		this.service=service;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class)
	public @ResponseBody ResponseEntity<ListRestResponse<ExternalSystem>> findAll(
			@RequestParam(name="systemTypeCode", required=true) String systemTypeCode) {
		ListRestResponse<ExternalSystem> body = RestResponseBuilder.getResponse(service.findBySystenType(systemTypeCode));
		return ResponseEntity.ok(body);
	}

}
