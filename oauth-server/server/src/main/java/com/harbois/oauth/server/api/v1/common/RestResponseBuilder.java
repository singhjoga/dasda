package com.harbois.oauth.server.api.v1.common;

import java.util.List;

import com.harbois.oauth.server.api.v1.common.RestResponse.AddResult;
import com.harbois.oauth.server.api.v1.common.RestResponse.Error;
import com.harbois.oauth.server.api.v1.common.RestResponse.GeneralErrorDetail;

public class RestResponseBuilder{
	public static RestResponse addResponse(String id, String location) {
		RestResponse resp = new RestResponse();
		AddResult result = new AddResult(id, location);
		resp.setResult(result);
		return resp;
	}
	public static RestResponse noResultResponse() {
		RestResponse resp= new RestResponse();
		return resp;
	}
	public static RestResponse getResponse(Object obj ) {
		RestResponse resp= new RestResponse();
		resp.setResult(obj);
		return resp;
	}
	public static RestResponse errorResponse(String message, String errorCode) {
		return errorResponse(message, errorCode,null);
	}	
	public static RestResponse errorResponse(String message, String errorCode,  List<Error> errors) {
		RestResponse resp= new RestResponse();
		resp.setErrorDetail(new GeneralErrorDetail(message, errorCode, errors));
		
		return resp;
	}
}
