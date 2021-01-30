package net.devoat.common.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.devoat.common.validation.ValidationContext;
import net.devoat.common.validation.annotations.UpdatePolicy;

public class UpdatePolicyValidator implements ConstraintValidator<UpdatePolicy, Object>{

	private UpdatePolicy annotation;
	
	@Override
	public void initialize(UpdatePolicy constraintAnnotation) {
		this.annotation=constraintAnnotation;
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value==null) return true;
		boolean isUpdate = ValidationContext.getInstance().isUpdate();
		if (isUpdate) {
			if (!annotation.updateable()) {
				//field is not updateable but there is value
				return false;
			}
		}else {
			if (!annotation.insertable()) {
				//field is not insertable but there is value
				return false;
			}			
		}
		return true;
	}

}
