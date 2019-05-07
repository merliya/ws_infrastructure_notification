package com.jbhunt.infrastructure.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.biz.securepid.PIDCredentials;
import com.jbhunt.infrastructure.notification.helper.SecurityContextPopulator;
import com.jbhunt.infrastructure.notification.properties.NotificationApplicationProperties;
import com.jbhunt.infrastructure.notification.service.NotificationUnsubscribeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class NotificationUnsubscribeController {
	private final NotificationUnsubscribeService notificationUnsubscribeService;
	private final NotificationApplicationProperties notificationApplicationProperties;
	private PIDCredentials pidCredentials;

	public NotificationUnsubscribeController(NotificationUnsubscribeService notificationUnsubscribeService, 
			NotificationApplicationProperties notificationApplicationProperties, PIDCredentials pidCredentials){
		this.notificationUnsubscribeService = notificationUnsubscribeService;
		this.notificationApplicationProperties = notificationApplicationProperties;
		this.pidCredentials = pidCredentials;
	}

	@GetMapping(value="/subscriptions/usersubscriptions/externaluser/unsubscribe/{id}")
	public String unSubscribeExternalNotification(@PathVariable String id){
		log.info("Inside Unsubscribe service"+ id);
		SecurityContextPopulator.setSecurityContextAuthentication(pidCredentials);
		String statusMessage = null;
		try {
			notificationUnsubscribeService.unSubscribeNotification(Integer.parseInt(id.trim()));
			log.info("log of return message in unsubscribe"+notificationApplicationProperties.getUnsubscribeSuccessMessage());
			statusMessage = notificationApplicationProperties.getUnsubscribeSuccessMessage();
		} catch(Exception e) {
			log.error("Exception in External unsubscribe",e);
			statusMessage = notificationApplicationProperties.getUnsubscribeFailureMessage();
		}
		return statusMessage;
	}
	
	@GetMapping(value="/subscriptions/usersubscriptions/internaluser/unsubscribe/{id}")
	public String unsubscribeInternalNotification(@PathVariable String id){
		log.info("Inside Unsubscribeinapp service"+ id);
		String statusMessage = null;
		try {
			notificationUnsubscribeService.unSubscribeNotification(Integer.parseInt(id.trim()));
			log.info("log of return message in unsubscribe"+notificationApplicationProperties.getUnsubscribeSuccessMessage());
			statusMessage = notificationApplicationProperties.getUnsubscribeSuccessMessage();
		} catch(Exception e) {
			log.error("Exception in Internal unsubscribe",e);
			statusMessage = notificationApplicationProperties.getUnsubscribeFailureMessage();
		}
		return statusMessage;
	}
}
