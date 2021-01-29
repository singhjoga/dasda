package com.harbois.oauth.server.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.harbois.oauth.server.validation.ValidationContext;
import com.harbois.oauth.server.validation.annotations.NotNullable;

public class NotNullableValidator implements ConstraintValidator<NotNullable, Object>{

	private NotNullable annotation;
	
	@Override
	public void initialize(NotNullable constraintAnnotation) {
		this.annotation=constraintAnnotation;
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean isUpdate = ValidationContext.getInstance().isUpdate();
		if (value != null) return true;
		if (isUpdate && annotation.applyOn().onUpdate()) { //update and not nullable for update
			return false;
		}else if (!isUpdate && annotation.applyOn().onInsert()) { //insert and not nullable for insert
			return false;
		}
		return true;
	}

}
