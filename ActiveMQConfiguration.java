package com.jbhunt.infrastructure.notification.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.properties.ActiveMQProperties;

@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
public class ActiveMQConfiguration {

	private final PIDCredentials pidCredentials;
	private final ActiveMQProperties activeMQProperties;

	
	public ActiveMQConfiguration(PIDCredentials pidCredentials, ActiveMQProperties activeMQProperties) {
		this.pidCredentials = pidCredentials;
		this.activeMQProperties = activeMQProperties;
	}

	@Bean
	public ActiveMQComponent activemq() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(activeMQProperties.getProducerBrokerURL());
		activeMQConnectionFactory.setUserName(pidCredentials.getUsername());
		activeMQConnectionFactory.setPassword(pidCredentials.getPassword());

		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
		pooledConnectionFactory.setMaxConnections(activeMQProperties.getMaxConnections());

		ActiveMQComponent activeMQComponent = new ActiveMQComponent();
		activeMQComponent.setConnectionFactory(pooledConnectionFactory);
		activeMQComponent.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONNECTION);
		activeMQComponent.setTransacted(false);
		return activeMQComponent;
	}

}