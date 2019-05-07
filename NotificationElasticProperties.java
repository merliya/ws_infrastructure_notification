package com.jbhunt.infrastructure.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@RefreshScope
@Component
@ConfigurationProperties
public class NotificationElasticProperties {

	private String elasticSearchBaseUrl;
	private String elasticSearchIndex;
	private String employeeIndex;
	private String elasticSearchType;
	private String employeeType;
	private Integer jestReadTimeout;
	private Integer jestDfltMaxTotalConnPerRoute;
	private Integer jestMaxTotalConnection;
	private String elasticProperty;
	private String employeeProperty;

}
