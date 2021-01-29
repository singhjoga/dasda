package com.harbois.oauth.server.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.harbois.oauth.server.spring.interceptors.ContextHandler;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Autowired
	private ContextHandler contextHandler;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(contextHandler);
	}	
}