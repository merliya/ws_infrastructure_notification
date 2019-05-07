package com.jbhunt.infrastructure.notification.controller;

import java.util.List;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jbhunt.infrastructure.notification.entity.NotificationSubCategory;
import com.jbhunt.infrastructure.notification.service.UserNotificationSubscriptionTypeAheadService;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationParameterDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationTypeDTO;

@RepositoryRestController
@RequestMapping(value = "/usernotifications")
public class UserNotificationSubscriptionTypeAheadController {

	private final UserNotificationSubscriptionTypeAheadService userNotificationSubscriptionTypeAheadService;

	public UserNotificationSubscriptionTypeAheadController(
			UserNotificationSubscriptionTypeAheadService userNotificationSubscriptionTypeAheadService) {
		this.userNotificationSubscriptionTypeAheadService = userNotificationSubscriptionTypeAheadService;
	}

	@GetMapping("/notificationsubcategories")
	public ResponseEntity<List<NotificationSubCategory>> fetchNotificationSubCategories(
			@RequestParam(value = "notificationCategories", required = false) List<String> notificationCategories) {
		return ResponseEntity.ok(
				userNotificationSubscriptionTypeAheadService.fetchNotificationSubCategories(notificationCategories));
	}

	@GetMapping("/notificationtypes")
	public ResponseEntity<List<UserNotificationTypeDTO>> fetchNotificationTypes(
			@RequestParam(value = "notificationCategories", required = false) List<String> notificationCategories,
			@RequestParam(value = "notificationSubCategories", required = false) List<String> notificationSubCategories,
			@RequestParam(value = "notificationType", required = false) String notificationType) {
		return ResponseEntity.ok(userNotificationSubscriptionTypeAheadService
				.fecthNotificationTypes(notificationCategories, notificationSubCategories, notificationType));
	}

	@GetMapping("/fetchnotificationtypesbyids/{notificationTypeIds}")
	public ResponseEntity<List<UserNotificationTypeDTO>> fetchNotificationTypesByIds(
			@PathVariable List<Integer> notificationTypeIds) {
		return ResponseEntity.ok(userNotificationSubscriptionTypeAheadService
				.fecthNotificationTypeswithIds(notificationTypeIds));
	}

	@GetMapping("/notificationparameters")
	public ResponseEntity<List<UserNotificationParameterDTO>> fetchNotificationParameters(
			@RequestParam(value = "notificationCategory", required = false) String notificationCategory,
			@RequestParam(value = "notificationSubCategory", required = false) String notificationSubCategory) {
		return ResponseEntity.ok(userNotificationSubscriptionTypeAheadService
				.fetchNotificationParameters(notificationCategory,notificationSubCategory));
	}

	@GetMapping("/status")
	public ResponseEntity<List<String>> fetchSubscriptionStatus() {
		return ResponseEntity.ok(userNotificationSubscriptionTypeAheadService.fetchSubscriptionStatus());
	}

	@GetMapping("/documents")
	public ResponseEntity<List<String>> fetchDocuementTypes() {
		return ResponseEntity.ok(userNotificationSubscriptionTypeAheadService.fetchDocumentTypes());
	}

}
