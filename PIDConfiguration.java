package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jbhunt.biz.securepid.FusePIDReader;
import com.jbhunt.biz.securepid.PIDCredentials;

@Configuration
public class PIDConfiguration {

	@Bean
	public PIDCredentials pidCredentials() {
		FusePIDReader fusePIDReader = new FusePIDReader("Infrastructure_platform");
		return fusePIDReader.readPIDCredentials("platformInfra");
	}

}
