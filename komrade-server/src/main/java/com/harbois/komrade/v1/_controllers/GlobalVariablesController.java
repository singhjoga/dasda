package com.harbois.komrade.v1._controllers;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.constants.Entities;
import com.harbois.komrade.v1.common.controllers.BaseEnvPropertiesController;
import com.harbois.komrade.v1.globalvars.GlobalVariable;
import com.harbois.komrade.v1.globalvars.GlobalVariableService;
import com.harbois.komrade.v1.globalvars.GlobalVariableValue;
import com.harbois.oauth.api.v1.common.RawData;
import com.harbois.oauth.api.v1.common.RestResponse.ListRestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.NoResultRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.security.Authorization;

@RestController
@RequestMapping("/api/v1/global-variables")
@Authorization(entity=Entities.GLOBAL_VARIABLE)
public class GlobalVariablesController extends BaseEnvPropertiesController<GlobalVariable, GlobalVariableValue> {
	private GlobalVariableService service;
	@Autowired
	public GlobalVariablesController(GlobalVariableService service) {
		super(service);
		this.service=service;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/allwithvalues")
	@Authorization(action=CrudActions.VIEW)
	@JsonView(value=Views.List.class) 
	public @ResponseBody ResponseEntity<ListRestResponse<GlobalVariable>> getAllWithValue(
			@RequestParam(value = "envCode", required = true) String[] envCodes) {
		Set<String> set = new TreeSet<>(Arrays.asList(envCodes));
		ListRestResponse<GlobalVariable> body = RestResponseBuilder.getResponse(service.getAllWithValues(set));
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.POST, value = "/bulkupdate/{envCode}")
	@Authorization(action=CrudActions.UPDATE)
	public @ResponseBody ResponseEntity<NoResultRestResponse> bulkUpdate(
			@PathVariable("envCode") String envCode,
			@RequestBody RawData rawData) {

		service.updateFromRawData(envCode, rawData);
		NoResultRestResponse body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
}
