package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jbhunt.infrastructure.notification.eum.ws.ObjectFactory;

@Configuration
public class EUMSoapConfiguration {

	@Bean
	public ObjectFactory eumObjectFactory() {
		return new ObjectFactory();
	}

}
