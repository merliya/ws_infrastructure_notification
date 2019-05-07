package com.jbhunt.infrastructure.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@RefreshScope
@Component
@ConfigurationProperties("enrichment")
@Data
public class EnrichmentProperties {
	private String accountBaseURL;
	private String locationBaseURL;
	private String referenceBaseURL;
	private String orderURL;
	private String locationURL;
	private String profileSearchURL;
	private String marketingAreaURL;
	private String businessUnit;
	private String serviceOffering;
	private String timeoutInMilliseconds;
	private String customerURL;
	private String timeDelay;
	private String tradingPartnerBaseURL;
	private String tradingPartnerDocType;
	private String taskAssignmentBaseURL;
	private String taskAssignmentURL;

}
