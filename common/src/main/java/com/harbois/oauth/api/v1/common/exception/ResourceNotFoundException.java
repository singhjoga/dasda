package com.harbois.oauth.api.v1.common.exception;

public class ResourceNotFoundException extends BadRequestException{

	private static final long serialVersionUID = -12069870686023303L;

	public ResourceNotFoundException(String resourceName, Long id) {
		this(resourceName, id.toString());
	}
	public ResourceNotFoundException(String resourceName, String id) {
		super("Resource not found "+resourceName+" for id "+id, ErrorCodes.RESOURCE_NOT_FOUND);
	}
	public ResourceNotFoundException(String msg) {
		super(msg,ErrorCodes.RESOURCE_NOT_FOUND);
	}

}
