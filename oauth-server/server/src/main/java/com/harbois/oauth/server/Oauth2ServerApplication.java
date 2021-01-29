package com.harbois.oauth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class Oauth2ServerApplication extends WebSecurityConfigurerAdapter {
	private static ApplicationContext ctx;
	public static void main(String[] args) {
		ctx = SpringApplication.run(Oauth2ServerApplication.class, args);
	}

	public static ApplicationContext applicationContext() {
		return ctx;
	}
}
