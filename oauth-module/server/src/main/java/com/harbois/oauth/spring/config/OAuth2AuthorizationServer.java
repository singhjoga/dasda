package com.harbois.oauth.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.harbois.oauth.authentication.MultiAuthenticationProvider;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter 
{
	@Autowired
    private DataSource ds;
	@Autowired
	private MultiAuthenticationProvider authProvider;	
	
	private AuthenticationManager authenticationManagerBean;
  	@Autowired
  	public void setAuthenticationManagerBean(AuthenticationManager authenticationManagerBean) {
  		this.authenticationManagerBean = authenticationManagerBean;
  	}
  	@Bean
    public TokenStore tokenStore() {
        return new CustomJdbcTokenStore(ds);
    }
 	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	/*	security
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()")
			.allowFormAuthenticationForClients();
			*/
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.jdbc(ds);
	}
	@Override
  	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
  		endpoints.authenticationManager(authenticationManagerBean);
  		endpoints.tokenStore(tokenStore());
  		endpoints.userDetailsService(authProvider);
  	}
}
