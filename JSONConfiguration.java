
package com.jbhunt.infrastructure.notification.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JSONConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
		Jackson2ObjectMapperBuilder objectMapperBuilder = new Jackson2ObjectMapperBuilder();
		objectMapperBuilder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		objectMapperBuilder.featuresToEnable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		return objectMapperBuilder;
	}
}
