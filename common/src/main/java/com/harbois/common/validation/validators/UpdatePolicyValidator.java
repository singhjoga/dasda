package com.harbois.common.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.harbois.common.validation.ValidationContext;
import com.harbois.common.validation.annotations.UpdatePolicy;

public class UpdatePolicyValidator implements ConstraintValidator<UpdatePolicy, Object>{

	private UpdatePolicy annotation;
	
	@Override
	public void initialize(UpdatePolicy constraintAnnotation) {
		this.annotation=constraintAnnotation;
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean isUpdate = ValidationContext.getInstance().isUpdate();
		if (isUpdate) {
			if (value != null && !annotation.updateable()) {
				//field is not updateable but there is value
				return false;
			}
		}else {
			if (value != null && !annotation.insertable()) {
				//field is not insertable but there is value
				return false;
			}			
		}
		return true;
	}

}
