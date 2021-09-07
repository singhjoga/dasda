package net.devoat.component.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebConfig1 extends WebSecurityConfigurerAdapter{
	private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v2/api-docs**",
            "/webjars/**","/configuration/ui","/configuration/security"
    };
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
         .anyRequest()
         .permitAll()
         .and().csrf().disable();
	}

}
