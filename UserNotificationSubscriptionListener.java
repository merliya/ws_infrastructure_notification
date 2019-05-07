package com.jbhunt.infrastructure.notification.handler;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.camel.ProducerTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.hrms.eoi.api.dto.person.PersonDTO;
import com.jbhunt.infrastructure.notification.constants.NotificationApplicationConstants;
import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscription;
import com.jbhunt.infrastructure.notification.helper.ProcessEnrichmentData;
import com.jbhunt.infrastructure.notification.properties.NotificationApplicationProperties;
import com.jbhunt.infrastructure.notification.service.UserNotificationSubscriptionService;
import com.jbhunt.infrastructure.notification.user.dto.UserDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserNotificationSubscriptionListener {

	private final NotificationApplicationProperties notificationApplicationProperties;
	private final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;
	private final ProducerTemplate producerTemplate;
	private final ProcessEnrichmentData processEnrichmentData;
	private PIDCredentials pidCredentials;
	private final UserNotificationSubscriptionService userNotificationSubscriptionService;

	public UserNotificationSubscriptionListener(NotificationApplicationProperties notificationApplicationProperties,
			Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder, ProducerTemplate producerTemplate,
			ProcessEnrichmentData processEnrichmentData, PIDCredentials pidCredentials, UserNotificationSubscriptionService userNotificationSubscriptionService) {
		this.notificationApplicationProperties = notificationApplicationProperties;
		this.jackson2ObjectMapperBuilder = jackson2ObjectMapperBuilder;
		this.producerTemplate = producerTemplate;
		this.processEnrichmentData = processEnrichmentData;
		this.pidCredentials = pidCredentials;
		this.userNotificationSubscriptionService = userNotificationSubscriptionService;
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUserNotificationSubscriptionEvent(
			CreationUserNotificationSubscription<UserNotificationSubscription> creationUserNotificationSubscription) {
		log.debug("Inside UserNotificationSubscriptionListener:handleUserNotificationSubscriptionEvent");

		UserNotificationSubscription userNotificationSubscription = creationUserNotificationSubscription
				.fetchUserNotificationSubscription();
		UserNotificationSubscriptionDTO userNotificationSubscriptionDTO = userNotificationSubscriptionService.getUserSubscriptions(userNotificationSubscription.getUserNotificationSubscriptionID());
		

		if (userNotificationSubscriptionDTO != null) {
			processEnrichmentData.setUserDTO(userNotificationSubscriptionDTO.getUserNotificationSubscriptionDetails());
			PersonDTO createdUserDetails = processEnrichmentData
					.getUserName(userNotificationSubscriptionDTO.getCreatedBy().getId());
			if (Objects.nonNull(createdUserDetails)) {
				setCreatedByDetails(userNotificationSubscriptionDTO, createdUserDetails);
			}
			
			if(pidCredentials.getUsername().equalsIgnoreCase(userNotificationSubscriptionDTO.getLastUpdatedBy().getId())) {
				setLastUpdatedByDetailsForUnsubscribe(userNotificationSubscriptionDTO);
			}else {
				PersonDTO updatedUserDetails = processEnrichmentData
						.getUserName(userNotificationSubscriptionDTO.getLastUpdatedBy().getId());
				if (Objects.nonNull(updatedUserDetails)) {
					setLastUpdatedByDetails(userNotificationSubscriptionDTO, updatedUserDetails);
				}
			}

			LocalDateTime expirationTimestamp = userNotificationSubscription.getExpirationTimestamp();
			LocalDateTime currentTimestamp = LocalDateTime.now();
			String status = expirationTimestamp.isAfter(currentTimestamp) == Boolean.TRUE
					? NotificationApplicationConstants.ACTIVE_STATUS : NotificationApplicationConstants.INACTIVE_STATUS;
			userNotificationSubscriptionDTO.setStatus(status);
			try {
				producerTemplate.sendBodyAndHeader(
						"activemq://topic:" + notificationApplicationProperties.getTopicName(),
						jackson2ObjectMapperBuilder.build().writeValueAsString(userNotificationSubscriptionDTO), "id",
						userNotificationSubscriptionDTO.getSubscriptionID());
			} catch (JsonProcessingException e) {
				log.error("Could not convert User Notification Subscription to JSON Content: ", e);
			}
		}

	}

	

	/**
	 * @param userNotificationSubscriptionDTO
	 * @param eoiUserDetails
	 */
	private void setCreatedByDetails(UserNotificationSubscriptionDTO userNotificationSubscriptionDTO,
			PersonDTO userDetails) {
		UserDTO createdBy = userNotificationSubscriptionDTO.getCreatedBy();
		createdBy.setFirstName(userDetails.getFirstName());
		createdBy.setLastName(userDetails.getLastName());
		createdBy.setPreferredName(userDetails.getPrefName());
		createdBy.setEmailAddress(userDetails.getEmail());
		userNotificationSubscriptionDTO.setCreatedBy(createdBy);
	}

	/**
	 * @param userNotificationSubscriptionDTO
	 * @param eoiUserDetails
	 */
	private void setLastUpdatedByDetails(UserNotificationSubscriptionDTO userNotificationSubscriptionDTO,
			PersonDTO userDetails) {
		UserDTO updatedBy = userNotificationSubscriptionDTO.getLastUpdatedBy();
		updatedBy.setFirstName(userDetails.getFirstName());
		updatedBy.setLastName(userDetails.getLastName());
		updatedBy.setPreferredName(userDetails.getPrefName());
		updatedBy.setEmailAddress(userDetails.getEmail());
		userNotificationSubscriptionDTO.setLastUpdatedBy(updatedBy);
	}
	
	private void setLastUpdatedByDetailsForUnsubscribe(UserNotificationSubscriptionDTO userNotificationSubscriptionDTO) {
		UserDTO updatedBy = userNotificationSubscriptionDTO.getLastUpdatedBy();
		updatedBy.setFirstName("System");
		userNotificationSubscriptionDTO.setLastUpdatedBy(updatedBy);
	}
}
