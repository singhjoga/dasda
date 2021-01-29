package com.harbois.common.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.harbois.common.validation.validators.NotNullableValidator;

@Target( {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotNullableValidator.class)
public @interface NotNullable {
    String message() default "{field.notnullable}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
    ApplicableType applyOn() default ApplicableType.OnlyOnInsert;
}