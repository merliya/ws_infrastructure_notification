package com.jbhunt.infrastructure.notification.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@RefreshScope
@Component
@ConfigurationProperties
public class NotificationApplicationProperties {

	@Value("#{'${externalUsers.roles}'.split(',')}")
	private List<String> externalUsersRoles;
	
	@Value("${externalUsers.url}")
	private String eumUrl;
	
	@Value("#{'${subscription.status}'.split(',')}")
	private List<String> subscriptionStatus;
	
	@Value("${pool.core.exactTargetAggregation}")
	private Integer exactTargetAggregationCorePoolSize;
	
	@Value("${pool.maximum.exactTargetAggregation}")
	private Integer exactTargetAggregationMaximumPoolSize;
	
	Map<String, List<String>> notificationParameterClassification;
	
	@Value("#{'${userNotificationSubscription.documentTypes}'.split(',')}")
	private List<String> documentTypes;
	
	@Value("${userNotificationSubscription.topic.name}")
	private String topicName;
	
	@Value("${userNotificationSubscription.systemTopic.name}")
	private String systemTopicName;
	
	@Value("${userNotificationSubscription.inAppTopic.name}")
	private String inAppTopic;
	
	@Value("${userNotificationSubscription.topic.ediName}")
	private String ediTopicName;

	@Value("${userNotificationSubscription.emailQueue.name}")
	private String emailQueueName;

	@Value("${userNotificationSubscription.errorReprocessingTopic.name}")
	private String errorReprocessingTopicName;

	@Value("${ediNotification.shipmentStatusTopic.name}")
	private String shipmentStatusTopicName;

	@Value("${ediNotification.loadTenderTopic.name}")
	private String loadTenderTopicName;

	@Value("${adminservice.baseURL}")
	private String adminBaseURL;
	
	@Value("${unsubscribe.successMessage}")
	private String unsubscribeSuccessMessage;
	
	@Value("${unsubscribe.failureMessage}")
	private String unsubscribeFailureMessage;

	@Value("${unsubscribe.apiUrl}")
	private String unsubscribeApiUrl;

	@Value("${unsubscribe.callbackUrl}")
	private String callbackUrl;

	@Value("${unsubscribe.oauthTokenUrl}")
	private String oauthTokenUrl;

	@Value("${unsubscribe.clientId}")
	private String clientId;

	@Value("${unsubscribe.clientSecret}")
	private String clientSecret;

	@Value("${unsubscribe.subscriptionKey}")
	private String subscriptionKey;
	
}
