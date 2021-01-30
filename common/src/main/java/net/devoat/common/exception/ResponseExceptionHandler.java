package net.devoat.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.devoat.common.RestResponse;
import net.devoat.common.RestResponseBuilder;
import net.devoat.common.RestResponse.NoResultRestResponse;
import net.devoat.common.RestResponse.ValidationError;

@RestControllerAdvice
@Component
public class ResponseExceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ResponseExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public NoResultRestResponse handleResourceNotFound(ResourceNotFoundException e) {
		LOG.warn(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), e.getErrorCode());
	}

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public NoResultRestResponse handleTechnicalException(IllegalStateException e) {
		LOG.error(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), ErrorCodes.INTERNAL_ERROR);
	}	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public NoResultRestResponse handleAccessDenied(AccessDeniedException e) {
		LOG.warn(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), ErrorCodes.ACCESS_DENIED);
	}	
	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public NoResultRestResponse handleAccessDenied(AuthorizationException e) {
		LOG.warn(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), e.getErrorCode());
	}	
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public NoResultRestResponse handleAccessDenied(BadRequestException e) {
		LOG.warn(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), e.getErrorCode());
	}	
	@ExceptionHandler(TechnicalException.class)
	@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
	public NoResultRestResponse handleTechnicalException(TechnicalException e) {
		LOG.warn(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), e.getErrorCode());
	}	

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public NoResultRestResponse handleAccessDenied(IllegalArgumentException e) {
		LOG.error(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), ErrorCodes.INTERNAL_ERROR);
	}
	@ExceptionHandler(BusinessRulesException.class)
	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	public NoResultRestResponse handleAccessDenied(BusinessRulesException e) {
		LOG.warn(e.getMessage());
		return RestResponseBuilder.errorResponse(e.getMessage(), e.getErrorCode());
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public NoResultRestResponse handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    List<ValidationError> errors = new ArrayList<RestResponse.ValidationError>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	    	FieldError fieldError = ((FieldError) error);
	        String fieldName = fieldError.getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.add(new ValidationError(fieldName, errorMessage, fieldError.getCode()));
	    });
	    return RestResponseBuilder.errorResponse("Validation errors", ErrorCodes.VALIDATION_ERROR,errors);
	}
	
}
