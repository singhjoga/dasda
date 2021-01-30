package net.devoat.component.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class AppConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
	   // return jacksonObjectMapperBuilder -> 
	    //    jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
		
		  return builder -> {
	            TimeZone tz = TimeZone.getTimeZone("UTC");
	            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	            df.setTimeZone(tz);

	            builder
	                  //  .failOnEmptyBeans(false)
	                   // .failOnUnknownProperties(false)
	                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
	                    .dateFormat(df);

	        };	
	}

}
