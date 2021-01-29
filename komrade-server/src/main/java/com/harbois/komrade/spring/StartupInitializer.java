package com.harbois.komrade.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupInitializer implements ApplicationListener<ApplicationContextEvent>{
	private static final Logger LOG = LoggerFactory.getLogger(StartupInitializer.class);

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			LOG.info("Initializing application...");
			LOG.info("Application initialization completed");
		}if (event instanceof ContextClosedEvent) {
			LOG.info("Closing KeePass Watch Service");

		}if (event instanceof ContextStartedEvent) {
			LOG.info("ContextStartedEvent");
		}if (event instanceof ContextStoppedEvent) {
			LOG.info("ContextStoppedEvent");
		}			
	}

}
