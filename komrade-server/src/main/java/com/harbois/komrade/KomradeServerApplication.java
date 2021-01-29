package com.harbois.komrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = "com.harbois")
@EnableJpaRepositories("com.harbois")
@EntityScan(basePackages= "com.harbois")
public class KomradeServerApplication extends WebSecurityConfigurerAdapter {
	private static ApplicationContext ctx;
	public static void main(String[] args) {
		ctx = SpringApplication.run(KomradeServerApplication.class, args);
	}

	public static ApplicationContext applicationContext() {
		return ctx;
	}
}
