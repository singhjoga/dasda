package com.harbois.komrade.v1.common.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.harbois.common.auth.CrudActions;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBased;
import com.harbois.komrade.v1.common.properties.PersistentPropertyEnvBasedValue;
import com.harbois.komrade.v1.common.services.BaseEnvPropertiesService;
import com.harbois.oauth.api.v1.common.RestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.AddRestResponse;
import com.harbois.oauth.api.v1.common.RestResponse.NoResultRestResponse;
import com.harbois.oauth.api.v1.common.RestResponseBuilder;
import com.harbois.oauth.api.v1.common.Views;
import com.harbois.oauth.security.Authorization;

public class BaseEnvPropertiesController<T extends PersistentPropertyEnvBased<T,V>, V extends PersistentPropertyEnvBasedValue<V>> extends BaseEntityController<T, String> {
	private BaseEnvPropertiesService<T, V> service;
	@Autowired
	public BaseEnvPropertiesController(BaseEnvPropertiesService<T, V> service) {
		super(service);
		this.service=service;
	}
	@RequestMapping(method = RequestMethod.POST, value = "/values")
	@Authorization(action=CrudActions.UPDATE)
	public @ResponseBody ResponseEntity<AddRestResponse> addValue(@JsonView(value=Views.Add.class) @Valid @RequestBody V value) {

		String valueId= service.addValue(value);
		URI resUrl = getCurrentURI(valueId);
		AddRestResponse body = RestResponseBuilder.addResponse(valueId, resUrl.toString());
		return ResponseEntity.created(resUrl).body(body);
	}
	@RequestMapping(method = RequestMethod.PATCH, value = "/values/{id}")
	@Authorization(action=CrudActions.UPDATE)
	public @ResponseBody ResponseEntity<NoResultRestResponse> updateValue(@PathVariable String id, @JsonView(value=Views.Update.class) @Valid @RequestBody V value) {

		service.updateValue(id, value);
		NoResultRestResponse body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
	@RequestMapping(method = RequestMethod.GET, value = "/values/{id}")
	@Authorization(action=CrudActions.VIEW)
	 @JsonView(value=Views.View.class) 
	public @ResponseBody ResponseEntity<RestResponse<V>> getValue(@PathVariable String id) {
		RestResponse<V> body = RestResponseBuilder.getResponse(service.getValueById(id,true));
		return ResponseEntity.ok(body);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/values/{id}")
	@Authorization(action=CrudActions.UPDATE)
	public @ResponseBody ResponseEntity<NoResultRestResponse> deletePropertyValue(@PathVariable String id) {
		service.deleteValue(id);
		NoResultRestResponse body = RestResponseBuilder.noResultResponse();
		return ResponseEntity.ok(body);
	}
}
