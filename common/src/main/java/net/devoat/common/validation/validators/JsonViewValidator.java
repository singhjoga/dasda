package net.devoat.common.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.fasterxml.jackson.annotation.JsonView;

import net.devoat.common.Views;
import net.devoat.common.validation.ValidationContext;

public class JsonViewValidator implements ConstraintValidator<JsonView, Object>{

	private JsonView annotation;
	
	@Override
	public void initialize(JsonView constraintAnnotation) {
		this.annotation=constraintAnnotation;
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value==null) return true;
		boolean isUpdate = ValidationContext.getInstance().isUpdate();
		if (isUpdate) {
			if (!findView(Views.Update.class)) {
				//field is not updateable but there is value
				return false;
			}
		}else {
			if (!findView(Views.Add.class)) {
				//field is not insertable but there is value
				return false;
			}			
		}
		return true;
	}
	
	private boolean findView(Class<?> cls) {
		if (annotation.value() != null) {
			for (Class<?> viewCls: annotation.value()) {
				if (cls.isAssignableFrom(viewCls)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
