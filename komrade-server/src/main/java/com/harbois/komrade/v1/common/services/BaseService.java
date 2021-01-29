package com.harbois.komrade.v1.common.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService{

	public BaseService() {
		super();
	}

	public String getLoggedUser() {
		// Get logged in user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	
	}

}