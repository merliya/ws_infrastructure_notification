package com.jbhunt.infrastructure.notification.controller;

import javax.validation.Valid;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbhunt.infrastructure.notification.properties.NotificationApplicationProperties;
import com.jbhunt.infrastructure.notification.unifiedschema.dto.UnifiedNotificationEvent;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DefaultPlatformNotificationController {

	private final ProducerTemplate producerTemplate;
	
	private final NotificationApplicationProperties notificationApplicationProperties;

	/**
	 * Constructor for DefaultPlatformNotificationController
	 * 
	 * @param producerTemplate
	 */
	public DefaultPlatformNotificationController(ProducerTemplate producerTemplate,NotificationApplicationProperties notificationApplicationProperties) {
		this.producerTemplate = producerTemplate;
		this.notificationApplicationProperties = notificationApplicationProperties;
	}

	/**
	 * 
	 * @param unifiedNotificationEvent
	 * @return
	 * @throws JsonProcessingException 
	 * @throws CamelExecutionException 
	 */
	@PostMapping(value = "/defaultnotification")
	public ResponseEntity<HttpStatus> sendDefaultPlatformNotification(
			@Valid @RequestBody UnifiedNotificationEvent unifiedNotificationEvent) throws CamelExecutionException, JsonProcessingException {
		log.debug("DefaultPlatformNotificationController :: sendDefaultPlatformNotification");
		ObjectMapper objectMapper = new ObjectMapper();
		producerTemplate.sendBody("activemq:topic:"+notificationApplicationProperties.getSystemTopicName(), objectMapper.writeValueAsString(unifiedNotificationEvent));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
