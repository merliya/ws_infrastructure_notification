package com.jbhunt.infrastructure.notification.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;
import com.jbhunt.infrastructure.notification.interceptor.SendGridHttpHeaderInterceptor;
import com.jbhunt.infrastructure.notification.properties.SendGridProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SendGridRestTemplateConfiguration {

    public final SendGridProperties sendGridProperties;

    public SendGridRestTemplateConfiguration(SendGridProperties sendGridProperties) {
        this.sendGridProperties = sendGridProperties;
    }

    @Bean
    public RestTemplate sgRestTemplate() {
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
        restTemplate.setInterceptors(Collections.singletonList(getSGHttpHeaderInterceptor()));
        return restTemplate;
    }

    private ClientHttpRequestInterceptor getSGHttpHeaderInterceptor() {
        return new SendGridHttpHeaderInterceptor(sendGridProperties);
    }
}
