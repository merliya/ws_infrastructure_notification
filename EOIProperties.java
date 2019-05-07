package com.jbhunt.infrastructure.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@RefreshScope
@Component
@ConfigurationProperties("eoi")
@Data
public class EOIProperties {

	private Integer connectionPoolCount;
	
	private Integer defaultMaxConnectionPerRoute;
	
	private Integer connectionPerRouteForEOIDomain;
	
	private String domainName;
	
	private String employeeDetailsVersion;
	
	private String defaultVersion;
	
	private String baseUrl;
	
	private String employeeDetailUrl;
	
}
