package com.jbhunt.infrastructure.notification.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@RefreshScope
@Component
@ConfigurationProperties("messaging")
@Data
public class ActiveMQProperties {

	@Value("${jbhunt.infrastructure.jms.connectionFactory.activeMQ.producer.brokerURL}")
	private String producerBrokerURL;

	private int maxConnections;

}
