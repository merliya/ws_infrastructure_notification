package com.jbhunt.infrastructure.notification.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

@RefreshScope
@Component
@ConfigurationProperties("exacttarget")
@Data
public class ExactTargetProperties {

	private String clientId;
	
	private String clientSecret;
	
	private String requestTokenUrl;
	
	private String emailSendUrl;
	
	private String emailDeliveryRecordUrl;
	
	private String emailFromAddress;
	
	private String emailFromName;
	
	private String deliveryRequestType;
	
	private long msgDeliveryMinDuration;
	
	private String internalEmailToAddress;

	private String externalEmailToAddress;
	
	private String isTest;

}
