package com.harbois.komrade.v1._controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEntityController;
import com.harbois.komrade.v1.refdata.RefDataService;
import com.harbois.komrade.v1.refdata.RefDataSystem;
import com.harbois.komrade.v1.refdata.RefDataUser;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/refdata")
@Authorization(entity=Entities.REF_DATA)
public class ReferenceDataController extends BaseEntityController<RefDataUser, String> {
	private RefDataService service;
	@Autowired
	public ReferenceDataController(RefDataService service) {
		super(service);
		this.service=service;
	}

	@RequestMapping(method=RequestMethod.GET,value="/bytype/{referenceType}")
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class)
	public ResponseEntity<ListRestResponse<RefDataUser>> findByUserReferenceType(@PathVariable String referenceType) {
		ListRestResponse<RefDataUser> body = RestResponseBuilder.getResponse(service.findByUserReferenceType(referenceType));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method=RequestMethod.GET,value="/system")
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class)
	public ResponseEntity<ListRestResponse<RefDataSystem>> findBySystemReferenceType() {
		ListRestResponse<RefDataSystem> body = RestResponseBuilder.getResponse(service.findAllSysReferenceType());
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method=RequestMethod.GET,value="/system/usertypes")
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class)
	public ResponseEntity<ListRestResponse<RefDataSystem>> getAllUserTypeNames() {
		ListRestResponse<RefDataSystem> body = RestResponseBuilder.getResponse(service.findAllUserRefDataTypeNames());
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method=RequestMethod.GET,value="/system/{referenceType}")
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class)
	public ResponseEntity<ListRestResponse<RefDataSystem>> findBySystemReferenceType(@PathVariable String referenceType) {
		ListRestResponse<RefDataSystem> body = RestResponseBuilder.getResponse(service.findBySysReferenceType(referenceType));
		return ResponseEntity.ok(body);
	}
}
