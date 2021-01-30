package net.devoat.oauth.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Authorization {
	String entity() default "";
	String[] hasAnyRoles() default {};
	String action() default "";
	String constraintVariables() default "";	   
}
