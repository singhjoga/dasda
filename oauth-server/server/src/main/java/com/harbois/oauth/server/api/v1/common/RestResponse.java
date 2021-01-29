package com.harbois.oauth.server.api.v1.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Common REST Response
 * @author JogaSingh
 *
 */
@JsonInclude(Include.NON_NULL)
public class RestResponse {
	private Object result;
	private WarningDetail warningDetail;
	private ErrorDetail<?> errorDetail;
	
	
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}

	public WarningDetail getWarningDetail() {
		return warningDetail;
	}
	public void setWarningDetail(WarningDetail warningDetail) {
		this.warningDetail = warningDetail;
	}
	public ErrorDetail<?> getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(ErrorDetail<?> errorDetail) {
		this.errorDetail = errorDetail;
	}
	public boolean hasErrors() {
		return this.errorDetail != null;
	}
	public boolean hasWarnings() {
		return this.warningDetail != null;
	}
	@SuppressWarnings("unchecked")
	public <T> T resultAs(Class<T> clz) {
		return (T)result;
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> resultAsList(Class<T> clz) {
		return (List<T>)result;
	}
	public static class WarningDetail extends MessageDetail{
		private List<Warning> warnings;
		public WarningDetail() {
			super(null,null);
		}
		public WarningDetail(String message, Set<String> warningList) {
			super(message, null);
			if (warningList != null && !warningList.isEmpty()) {
				warnings = new ArrayList<RestResponse.Warning>();
				for (String warnMsg: warningList) {
					warnings.add(new Warning(warnMsg,null));
				}
			}
		}
		public List<Warning> getWarnings() {
			return warnings;
		}
		public void setWarnings(List<Warning> warnings) {
			this.warnings = warnings;
		}
		
	}

	public static abstract class ErrorDetail<T extends Error> extends MessageDetail{
		private List<T> errors;		
		public ErrorDetail() {
			this(null, null,null);
		}
		public ErrorDetail(String message, String code, List<T> errors) {
			super(message, code);
			this.errors = errors;
		}
		public List<T> getErrors() {
			return errors;
		}
		public void setErrors(List<T>  errors) {
			this.errors = errors;
		}
	}
	public static class ValidationErrorDetail extends ErrorDetail<ValidationError> {
		public ValidationErrorDetail() {
			super();
		}
		public ValidationErrorDetail(String message, String code, List<ValidationError> errors) {
			super(message, code, errors);
		}
	}
	public static class GeneralErrorDetail extends ErrorDetail<Error>{
		public GeneralErrorDetail() {
			super();
		}
		public GeneralErrorDetail(String message, String code, List<Error> errors) {
			super(message, code, errors);
		}
	}
	public static class Warning extends MessageDetail{
		public Warning() {
			this(null, null);
		}
		public Warning(String message, String code) {
			super(message, code);
		}
	}
	public static class ValidationError extends Error{
		private String field;
		public ValidationError() {
			super(null, null);
		}
		public ValidationError(String field, String message, String code) {
			super(message, code);
			this.field=field;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		
	}
	public static class Error extends MessageDetail{		
		public Error() {
			this(null,null);
		}
		public Error(String message, String code) {
			super(message, code);
		}
	}
	public static class MessageDetail {
		private String message;
		private String code;
		public MessageDetail(String message, String code) {
			super();
			this.message = message;
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		
	}

	public static class AddResult {
		private String id;
		private String location;
		
		public AddResult() {
		}
		public AddResult(String id, String location) {
			this.id = id;
			this.location = location;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		
	}
}
