package com.jbhunt.infrastructure.notification.controller;

import javax.validation.Valid;

import com.jbhunt.infrastructure.notification.service.SendGridEmailNotificationService;
import com.jbhunt.infrastructure.notification.unifiedschema.dto.EmailNotification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.infrastructure.notification.service.NotificationDeliveryService;
import com.jbhunt.infrastructure.notification.service.SmsNotificationService;
import com.jbhunt.infrastructure.notification.unifiedschema.dto.UnifiedNotificationNonRecurringEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * NotificationDeliveryController provides service to deliver notifications
 * through various channels like email, sms, in-app, edi.
 * 
 * @author rcon335
 *
 */
@RestController
@Slf4j
public class NotificationDeliveryController {

	private final NotificationDeliveryService userNotificationDeliveryService;
	private final SmsNotificationService smsNotificationService;
	private final SendGridEmailNotificationService sendGridEmailNotificationService;
	
	public NotificationDeliveryController(NotificationDeliveryService userNotificationDeliveryService,
										  SmsNotificationService smsNotificationService,
										  SendGridEmailNotificationService sendGridEmailNotificationService) {
		this.userNotificationDeliveryService = userNotificationDeliveryService;
		this.smsNotificationService = smsNotificationService;
		this.sendGridEmailNotificationService = sendGridEmailNotificationService;
	}

	@PostMapping(value="/usernotifications")
	public ResponseEntity<String> deliverUserNotification(
			@Valid @RequestBody UnifiedNotificationNonRecurringEvent unifiedNotificationNonRecurringEvent)
			throws Exception {
		log.info("Deliver User Notifications channel: {} NotificationRawDataID : {}", unifiedNotificationNonRecurringEvent.getNotificationDeliveryChannel(),  
		        unifiedNotificationNonRecurringEvent.getNotificationRawDataID());
		userNotificationDeliveryService.deliverNotification(unifiedNotificationNonRecurringEvent);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@GetMapping("/sms/deliverystatus/{status}/{logId}")
	public ResponseEntity<String> updateSmsDeliveryStatus(
	        @PathVariable("status") String status, @PathVariable("logId") Integer logId){
	    smsNotificationService.updateStatus(status, logId);
	    return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(value="/emailnotification")
	public ResponseEntity<String> deliverEmailNotification(@Valid @RequestBody EmailNotification emailNotification) {
		sendGridEmailNotificationService.sendEmailNotification(emailNotification);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
