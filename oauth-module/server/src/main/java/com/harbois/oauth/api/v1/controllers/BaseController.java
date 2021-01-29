package com.harbois.oauth.api.v1.controllers;

import java.io.Serializable;
import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class BaseController {

	public BaseController() {
		super();
	}

	protected URI getCurrentURI() {
		return ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
	}

	protected URI getCurrentURI(Serializable id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().pathSegment(id.toString()). build().toUri();
	}

}