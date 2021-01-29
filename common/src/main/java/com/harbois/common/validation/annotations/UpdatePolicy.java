package com.harbois.common.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.harbois.common.validation.validators.UpdatePolicyValidator;

@Target( {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UpdatePolicyValidator.class)
public @interface UpdatePolicy {
    String message() default "{field.not.updateable}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
    boolean insertable() default true;
    boolean updateable() default true;
    boolean alwaysUpdate() default false;
}