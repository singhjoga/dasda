package com.harbois.oauth.server.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter 
{
	@Override
	public void configure(HttpSecurity http) throws Exception {
		/* Open everything except api. Static content is also served from this application
		 * Protect the API endpoints, /oauth is protected in the SecurityConfig
		 */

		http.authorizeRequests().antMatchers("/").permitAll();
		//http.authorizeRequests().antMatchers("/api/**").hasAnyAuthority(Constants.ROLE_SYS_ADMIN,Constants.ROLE_CLIENT_ADMIN,Constants.ROLE_CLIENT_ADMIN,Constants.ROLE_USER_MANAGER); // .authenticated();
		
		 http.headers().frameOptions().disable();		
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("oauth-api");
	}
	
}
