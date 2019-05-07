package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.properties.WebSocketProperties;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

	private final PIDCredentials pidCredentials;
	private final WebSocketProperties websocketProperties;

	
	public WebSocketConfiguration(PIDCredentials pidCredentials, WebSocketProperties websocketProperties) {
		this.pidCredentials = pidCredentials;
		this.websocketProperties = websocketProperties;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/messaging").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableStompBrokerRelay("/topic", "/queue").setRelayHost(websocketProperties.getRelayHost())
				.setRelayPort(websocketProperties.getRelayPort()).setVirtualHost(websocketProperties.getRelayHost()).setSystemLogin(pidCredentials.getUsername())
				.setSystemPasscode(pidCredentials.getPassword()).setClientLogin(pidCredentials.getUsername())
				.setClientPasscode(pidCredentials.getPassword());
		registry.setApplicationDestinationPrefixes("/platform");
	}

}
