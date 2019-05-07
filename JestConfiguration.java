package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.properties.NotificationElasticProperties;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

@Configuration
public class JestConfiguration {

	private final NotificationElasticProperties notificationElasticProperties;

	private final PIDCredentials pidCredentials;

	public JestConfiguration(NotificationElasticProperties notificationElasticProperties, PIDCredentials pidCredentials) {
		this.notificationElasticProperties = notificationElasticProperties;
		this.pidCredentials = pidCredentials;
	}

	@Bean
	public JestClient openClient() {
		HttpClientConfig clientConfig = new HttpClientConfig.Builder(
				notificationElasticProperties.getElasticSearchBaseUrl())
		                .readTimeout(notificationElasticProperties.getJestReadTimeout())
		                .connTimeout(notificationElasticProperties.getJestReadTimeout())
		                .defaultCredentials(pidCredentials.getUsername(), pidCredentials.getPassword())
		                .defaultMaxTotalConnectionPerRoute(
		                		notificationElasticProperties.getJestDfltMaxTotalConnPerRoute())
		                .maxTotalConnection(notificationElasticProperties.getJestMaxTotalConnection())
		                .multiThreaded(true).build();
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(clientConfig);
		return factory.getObject();
	}
}

