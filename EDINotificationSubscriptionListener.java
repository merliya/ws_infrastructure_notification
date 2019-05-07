package com.jbhunt.infrastructure.notification.handler;

import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationSubscriptionDTO;
import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;
import com.jbhunt.infrastructure.notification.helper.EDINotificationHelper;
import com.jbhunt.infrastructure.notification.mapper.EDINotificationSubscriptionMapper;
import com.jbhunt.infrastructure.notification.properties.NotificationApplicationProperties;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EDINotificationSubscriptionListener {

	private final EDINotificationSubscriptionMapper shipmentNotificationSubscriptionMapper;
	private final NotificationApplicationProperties notificationApplicationProperties;
	private final ProducerTemplate producerTemplate;
	private final EDINotificationHelper ediNotificationHelper;

	public EDINotificationSubscriptionListener(EDINotificationSubscriptionMapper shipmentNotificationSubscriptionMapper,
			NotificationApplicationProperties notificationApplicationProperties, ProducerTemplate producerTemplate,
			EDINotificationHelper ediNotificationHelper) {
		this.shipmentNotificationSubscriptionMapper = shipmentNotificationSubscriptionMapper;
		this.notificationApplicationProperties = notificationApplicationProperties;
		this.producerTemplate = producerTemplate;
		this.ediNotificationHelper = ediNotificationHelper;
	}

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleEDINotificationSubscriptionEvent(
			CreationEDINotificationSubscription<EDINotificationSubscription> creationEDINotificationSubscription) {
		log.info("handleEDINotificationSubscriptionListListEvent");
		EDINotificationSubscriptionDTO edinotificationSubscriptionDTO = shipmentNotificationSubscriptionMapper
				.ediNotificationSubscriptionDTO(creationEDINotificationSubscription.fetchEDINotificationSubscription(),
						creationEDINotificationSubscription.fetchTradingPartnerAssociatedPartyLocations());
		ediNotificationHelper.enrichBTODetails(edinotificationSubscriptionDTO);
		ediNotificationHelper.enrichLOBDetails(edinotificationSubscriptionDTO);
		ediNotificationHelper.enrichUserDetails(creationEDINotificationSubscription.fetchEDINotificationSubscription(), edinotificationSubscriptionDTO);
		ObjectMapper objectMapper = new ObjectMapper();
		String payload = null;
		try {
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			payload = objectMapper.writeValueAsString(edinotificationSubscriptionDTO);
		} catch (JsonProcessingException e) {
			log.error("Exception occured while parsing the EDINotificationSubscriptionDTO object : ", e);
		}
		log.info("EDI subscription post to elastic topic :" +notificationApplicationProperties.getEdiTopicName());
		producerTemplate.sendBodyAndHeader("activemq://topic:" + notificationApplicationProperties.getEdiTopicName(),
				payload, "id", edinotificationSubscriptionDTO.getShipmentNotificationSubscriptionID());
		log.info("EDI subscription update sucessfully posted to subscription topic");

	}

}
