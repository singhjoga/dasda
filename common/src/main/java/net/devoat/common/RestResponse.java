package net.devoat.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Common REST Response
 * @author JogaSingh
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
public class RestResponse<R> {
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private R result;
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private WarningDetail warningDetail;
	@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
	private ErrorDetail errorDetail;
	
	
	public R getResult() {
		return result;
	}
	public void setResult(R result) {
		this.result = result;
	}

	public WarningDetail getWarningDetail() {
		return warningDetail;
	}
	public void setWarningDetail(WarningDetail warningDetail) {
		this.warningDetail = warningDetail;
	}
	public ErrorDetail getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(ErrorDetail errorDetail) {
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
		@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
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

	public static class ErrorDetail extends MessageDetail{
		@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
		private List<ValidationError> errors;		
		public ErrorDetail() {
			this(null, null,null);
		}
		public ErrorDetail(String message, String code, List<ValidationError> errors) {
			super(message, code);
			this.errors = errors;
		}
		public List<ValidationError> getErrors() {
			return errors;
		}
		public void setErrors(List<ValidationError>  errors) {
			this.errors = errors;
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
		@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
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
		@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
		private String message;
		@JsonView(value= {Views.Add.class,Views.Update.class,Views.List.class})
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
	
	public static class AddRestResponse extends RestResponse<AddResult> {
		
	}
	public static class NoResultRestResponse extends RestResponse<Void> {
		
	}
	public static class ListRestResponse<T> extends RestResponse<List<T>> {
		
	}
}
