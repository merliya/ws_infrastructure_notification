package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.interceptor.BasicAuthInterceptor;

@Configuration
public class RestDataTemplateConfiguration {

	private PIDCredentials pidCredentials;

	public RestDataTemplateConfiguration(PIDCredentials pidCredentials) {
		this.pidCredentials = pidCredentials;
	}

	/**
	 * To get the RestTemplate instance
	 * 
	 * @return RestTemplate
	 */
	@Bean
	public RestTemplate restDataTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter messageMappingConverter = new MappingJackson2HttpMessageConverter();
		restTemplate.getMessageConverters().add(messageMappingConverter);
		Jaxb2RootElementHttpMessageConverter jaxbMessageConverter = new Jaxb2RootElementHttpMessageConverter();
		restTemplate.getMessageConverters().add(jaxbMessageConverter);
		BasicAuthInterceptor basicAuthInterceptor = new BasicAuthInterceptor(pidCredentials.getUsername(),
		pidCredentials.getPassword());
		restTemplate.getInterceptors().add(basicAuthInterceptor);
		return restTemplate;
	}
}
