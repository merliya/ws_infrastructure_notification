package com.jbhunt.infrastructure.notification.configuration;

import com.jbhunt.biz.securepid.PIDCredentials;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FavoriteRestTemplateConfiguration {
    @Bean(name="favoriteRestTemplate")
    public RestTemplate favoriteRestTemplate(RestTemplateBuilder builder, final PIDCredentials credentials) {
        return builder.basicAuthorization(credentials.getUsername(), credentials.getPassword()).build();
    }
}
