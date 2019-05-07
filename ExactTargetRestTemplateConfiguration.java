package com.jbhunt.infrastructure.notification.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;
import com.jbhunt.infrastructure.notification.interceptor.ExactTargetHttpHeaderInterceptor;
import com.jbhunt.infrastructure.notification.service.ExactTargetOAuthAccessTokenService;

@Configuration
public class ExactTargetRestTemplateConfiguration {

	private final ExactTargetOAuthAccessTokenService etOAuthAccessTokenService;

	public ExactTargetRestTemplateConfiguration(ExactTargetOAuthAccessTokenService etOAuthAccessTokenService) {
		this.etOAuthAccessTokenService = etOAuthAccessTokenService;
	}

	@Bean
	public RestTemplate etRestTemplate() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());
		
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes(NotificationApplicationConstants.MEDIA_TYPE_JSON));
		converter.setObjectMapper(mapper);
		
		final RestTemplate restTemplate = new RestTemplate(Arrays.asList(converter));
		final HttpClient httpClient = HttpClientBuilder.create().build();
		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate.setRequestFactory(requestFactory);
		restTemplate.setInterceptors(Collections.singletonList(getETHttpHeaderInterceptor()));
		return restTemplate;
	}

	private ClientHttpRequestInterceptor getETHttpHeaderInterceptor() {
		return new ExactTargetHttpHeaderInterceptor(etOAuthAccessTokenService);
	}

}
