package com.jbhunt.infrastructure.notification.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;
import com.jbhunt.infrastructure.notification.interceptor.HttpHeaderInterceptor;
import com.jbhunt.infrastructure.notification.properties.NotificationApplicationProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfiguration {

	private NotificationApplicationProperties notificationApplicationProperties;

	public RestTemplateConfiguration(NotificationApplicationProperties notificationApplicationProperties) {
		this.notificationApplicationProperties = notificationApplicationProperties;
	}

	@Bean
	public RestTemplate restTemplate() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());
		
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes(NotificationApplicationConstants.MEDIA_TYPE_JSON));
		converter.setObjectMapper(mapper);
		
		final RestTemplate restTemplate = new RestTemplate(Collections.singletonList(converter));
		final HttpClient httpClient = HttpClientBuilder.create().build();
		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate.setRequestFactory(requestFactory);
		restTemplate.setInterceptors(Collections.singletonList(new HttpHeaderInterceptor()));
		return restTemplate;
	}
	
	@Bean
    public RestTemplate smsRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new HttpHeaderInterceptor()));
        return restTemplate;
    }

    @Bean
	public OAuth2RestTemplate oauth2RestTemplate(){

		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails ();
		resourceDetails.setAccessTokenUri(notificationApplicationProperties.getOauthTokenUrl());
		resourceDetails.setClientId(notificationApplicationProperties.getClientId());
		resourceDetails.setClientSecret(notificationApplicationProperties.getClientSecret());
		final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
		return  oAuth2RestTemplate;
	}

}
