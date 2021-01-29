package com.harbois.oauth.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.harbois.oauth.api.v1.common.UserContext;
import com.harbois.oauth.api.v1.common.utils.ReflectUtil;

@Component
@Aspect
public class AuthorizationAspect {
	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationAspect.class);

	
	@Before("@annotation(Authorization)")
	public void authorizeMethod(JoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
	
		Annotation anno = method.getAnnotation(Authorization.class);
		authorize(method,anno);
	}

	@Before("@within(Authorization) && !@annotation(Authorization)")
	public void authorizeClass(JoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Annotation anno = joinPoint.getTarget().getClass().getAnnotation(Authorization.class);
		authorize(method,anno);
	}
/*
	@Before("within(com.harbois.oauth.server.api.v1.controllers.*)")
	public void authorizeClass2(JoinPoint joinPoint) throws Throwable {
		// First see if the values are given in annotation
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		LOG.info("Within2: Method="+method.getName());
	
		Annotation anno = joinPoint.getTarget().getClass().getAnnotation(Authorization.class);
		//authorize(method,anno);
	}
*/
	private void authorize(Method method, Annotation anno) {
		if (anno==null) {
			return;
		}
		boolean authorized=false;
		String[] anyRoles = (String[]) ReflectUtil.getAnnotationFieldValue(anno, "anyRoles");
		LOG.info("Method: "+method.getName()+", anyRoles: "+anyRoles);
		if (anyRoles.length==0) {
			//no specific role, throw error if the user is not authenticated
			authorized = StringUtils.isEmpty(UserContext.getInstance().getUsername());
		}else {
			authorized=UserContext.getInstance().hasAnyRole(anyRoles);
		}
		if (!authorized) {
			throw new AccessDeniedException("Access denied. Username: "+UserContext.getInstance().getUsername());
		}
	}
}
