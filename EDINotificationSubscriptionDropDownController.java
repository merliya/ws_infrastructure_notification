package com.jbhunt.infrastructure.notification.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.infrastructure.notification.service.EDINotificationSubscriptionDropDownService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/subscriptions/edisubscriptions")
public class EDINotificationSubscriptionDropDownController {
	private final EDINotificationSubscriptionDropDownService eDINotificationSubscriptionDropDownService;
	public EDINotificationSubscriptionDropDownController(EDINotificationSubscriptionDropDownService eDINotificationSubscriptionDropDownService){
		this.eDINotificationSubscriptionDropDownService = eDINotificationSubscriptionDropDownService;
	}
	@GetMapping("/notificationshipmentholdstopreason/{stopType}")
	public Set<String> fetchStopReason(@PathVariable String stopType){
		return eDINotificationSubscriptionDropDownService.fetchStopReason(stopType);
		
	}
	
	@GetMapping("/notificationshipmentholdeventtype/{stopType}/{stopReason}")
	public Set<String> fetchEventType(@PathVariable String stopType, @PathVariable String stopReason){
		return eDINotificationSubscriptionDropDownService.fetchEventType(stopType, stopReason);		
	}
	
	
}
