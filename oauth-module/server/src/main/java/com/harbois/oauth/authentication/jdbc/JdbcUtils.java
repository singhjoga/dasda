package com.harbois.oauth.authentication.jdbc;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcUtils {

	public static DataSource createDataSource(JdbcAuthSettings settings) {
		 DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
	        dataSourceBuilder.driverClassName(settings.getDriverClass());
	        dataSourceBuilder.url(settings.getJdbcUrl());
	        dataSourceBuilder.username(settings.getUsername());
	        dataSourceBuilder.password(settings.getPassword());
	        return dataSourceBuilder.build();
	}
	
	public static JdbcTemplate createJdbcTemplate(JdbcAuthSettings settings) {
		DataSource ds = createDataSource(settings);
		JdbcTemplate template = new JdbcTemplate(ds);
		template.afterPropertiesSet();
		
		return template;
	}
}
