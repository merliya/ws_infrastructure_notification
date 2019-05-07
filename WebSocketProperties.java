package com.jbhunt.infrastructure.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@RefreshScope
@Component
@ConfigurationProperties("websocket")
@Data
public class WebSocketProperties {
	
	private String relayHost;
	
	private Integer relayPort;
	
	private String notificationTopic;
	
}
