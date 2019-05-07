package com.jbhunt.infrastructure.notification.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;
import com.jbhunt.infrastructure.notification.interceptor.EOIHttpHeaderInterceptor;
import com.jbhunt.infrastructure.notification.properties.EOIProperties;

@Configuration
public class EOIRestTemplateConfiguration {

	private final EOIProperties eoiProperties;
	private final PIDCredentials pidCredentials;

	@Autowired
	public EOIRestTemplateConfiguration(EOIProperties eoiProperties, PIDCredentials pidCredentials) {
		this.eoiProperties = eoiProperties;
		this.pidCredentials = pidCredentials;
	}

	@Bean
	public RestTemplate eoiRestTemplate() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes(NotificationApplicationConstants.MEDIA_TYPE_JSON));
		converter.setObjectMapper(mapper);
		final RestTemplate restTemplate = new RestTemplate(Arrays.asList(converter));

		final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(eoiProperties.getConnectionPoolCount());
		connManager.setDefaultMaxPerRoute(eoiProperties.getDefaultMaxConnectionPerRoute());
		connManager.setMaxPerRoute(new HttpRoute(new HttpHost(eoiProperties.getDomainName())),
				eoiProperties.getConnectionPerRouteForEOIDomain());
		final HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate.setRequestFactory(requestFactory);
		restTemplate.setInterceptors(Collections.singletonList(getEOIHttpHeaderInterceptor()));

		return restTemplate;
	}

	private ClientHttpRequestInterceptor getEOIHttpHeaderInterceptor() {
		return new EOIHttpHeaderInterceptor(pidCredentials, eoiProperties);
	}

}
