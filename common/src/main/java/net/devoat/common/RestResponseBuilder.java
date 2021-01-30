package net.devoat.common;

import java.util.List;

import net.devoat.common.RestResponse.AddRestResponse;
import net.devoat.common.RestResponse.AddResult;
import net.devoat.common.RestResponse.ErrorDetail;
import net.devoat.common.RestResponse.ListRestResponse;
import net.devoat.common.RestResponse.NoResultRestResponse;
import net.devoat.common.RestResponse.ValidationError;

public class RestResponseBuilder{
	public static AddRestResponse addResponse(String id, String location) {
		AddRestResponse resp = new AddRestResponse();
		AddResult result = new AddResult(id, location);
		resp.setResult(result);
		return resp;
	}
	public static NoResultRestResponse noResultResponse() {
		NoResultRestResponse resp= new NoResultRestResponse();
		return resp;
	}
	public static<T> RestResponse<T> getResponse(T obj ) {
		RestResponse<T> resp= new RestResponse<>();
		resp.setResult(obj);
		return resp;
	}
	public static<T> ListRestResponse<T> getResponse(List<T> obj ) {
		ListRestResponse<T> resp= new ListRestResponse<>();
		resp.setResult(obj);
		return resp;
	}
	
	public static NoResultRestResponse errorResponse(String message, String errorCode) {
		return errorResponse(message, errorCode,null);
	}	
	public static NoResultRestResponse errorResponse(String message, String errorCode,  List<ValidationError> errors) {
		NoResultRestResponse resp= new NoResultRestResponse();
		resp.setErrorDetail(new ErrorDetail(message, errorCode, errors));
		
		return resp;
	}

}
