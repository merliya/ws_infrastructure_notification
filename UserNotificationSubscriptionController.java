package com.jbhunt.infrastructure.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.infrastructure.notification.entity.UserNotificationSubscription;
import com.jbhunt.infrastructure.notification.service.UserNotificationSubscriptionService;
import com.jbhunt.infrastructure.usernotification.subscription.dto.UserNotificationSubscriptionDTO;

import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.List;

/**
 * NotificationUserSubscriptionController
 * 
 * @author rcon980
 *
 */
@Slf4j
@RestController
public class UserNotificationSubscriptionController {

	private final UserNotificationSubscriptionService userNotificationSubscriptionService;

	public UserNotificationSubscriptionController(
			UserNotificationSubscriptionService userNotificationSubscriptionService) {
		this.userNotificationSubscriptionService = userNotificationSubscriptionService;
	}

	/**
	 * 
	 * @param subscriptionID
	 *            Integer
	 * @return userNotificationSubscriptionDTO UserNotificationSubscriptionDTO
	 */
	@GetMapping(value = "/subscriptions/usersubscriptions/{subscriptionID}")
	public ResponseEntity<UserNotificationSubscriptionDTO> getUserSubscriptions(@PathVariable Integer subscriptionID) {
		log.info("Notification User Subscription Controller: Get Subscription Details ");
		UserNotificationSubscriptionDTO userNotificationSubscriptionDTO = userNotificationSubscriptionService
				.getUserSubscriptions(subscriptionID);
		return ResponseEntity.ok(userNotificationSubscriptionDTO);
	}

	@GetMapping(value ="/subscriptions/getsubscriptionscreatedByUser/{subscribedUserId}/{domainCode}")
	public  ResponseEntity<List<UserNotificationSubscriptionDTO>> getNotificationSubscriptionsCreatedByUser(@PathVariable String subscribedUserId,
																											@PathVariable String domainCode) {
		// log.info("Notification get user subscription:Get Subscription Details");
		List<UserNotificationSubscriptionDTO> userNotificationSubscriptionDTOList =userNotificationSubscriptionService.getSubscriptionsCreatedByUser(subscribedUserId, domainCode);

		return ResponseEntity.ok(userNotificationSubscriptionDTOList);
	}
	/**
	 * 
	 * @param subscriptionID
	 *            Integer
	 * @return Success Message String
	 */
	@PatchMapping(value = "/subscriptions/usersubscriptions/activate/{subscriptionID}")
	public ResponseEntity<UserNotificationSubscription> activateSubscription(@PathVariable Integer subscriptionID) {
		log.info("Notification User Subscription Controller: Activate Subscription");
		UserNotificationSubscription responseEntity = userNotificationSubscriptionService
				.activateSubscription(subscriptionID);
		return ResponseEntity.ok(responseEntity);
	}

	/**
	 * 
	 * @param subscriptionID
	 *            Integer
	 * @return Success Message String
	 */
	@PatchMapping(value = "/subscriptions/usersubscriptions/inactivate/{subscriptionID}")
	public ResponseEntity<UserNotificationSubscription> inactivateSubscription(@PathVariable Integer subscriptionID) {
		log.info("Notification User Subscription Controller: InActivate Subscription ");
		UserNotificationSubscription responseEntity = userNotificationSubscriptionService
				.inactivateSubscription(subscriptionID);
		return ResponseEntity.ok(responseEntity);
	}

	/**
	 * 
	 * @param userNotificationSubscriptionDTO
	 *            UserNotificationSubscriptionDTO
	 * @return UserNotificationSubscription userNotificationSubscription
	 */
	@PostMapping(value="/subscriptions/usersubscriptions")
	public UserNotificationSubscription createSubscriptionNotification(
			@RequestBody UserNotificationSubscriptionDTO userNotificationSubscriptionDTO) {
		log.info("Notification User Subscription Controller: createSubscriptionNotification ");
		UserNotificationSubscription userNotificationSubscription = userNotificationSubscriptionService.createUserNotificationSubscription(userNotificationSubscriptionDTO);
		if(userNotificationSubscription!=null){
			userNotificationSubscription.setApplication(null);
		}
		return userNotificationSubscription;
	}

	/**
	 * 
	 * @param userNotificationSubscriptionDTO
	 *            UserNotificationSubscriptionDTO
	 * @return UserNotificationSubscription userNotificationSubscription
	 */
	@PutMapping(value="/subscriptions/usersubscriptions")
	public UserNotificationSubscription editSubscriptionNotification(@RequestBody UserNotificationSubscriptionDTO userNotificationSubscriptionDTO) {
		log.info("Notification User Subscription Controller: editSubscriptionNotification ");
		UserNotificationSubscription userNotificationSubscription = userNotificationSubscriptionService.createUserNotificationSubscription(userNotificationSubscriptionDTO);
		if(userNotificationSubscription!=null){
			userNotificationSubscription.setApplication(null);
		}
		return userNotificationSubscription;

	}

	@PostMapping(value="/subscriptions/createsubscription")
	public UserNotificationSubscriptionDTO createUserNotificationSubscription(
			@RequestBody UserNotificationSubscriptionDTO userNotificationSubscriptionDTO) {
		log.info("Notification User Subscription Controller: createSubscription ");
		return userNotificationSubscriptionService.createUpdateUserNotificationSubscription(userNotificationSubscriptionDTO);


	}

	@PutMapping(value="/subscriptions/updatesubscription")
	public UserNotificationSubscriptionDTO updateUserNotificationSubscription(
			@RequestBody UserNotificationSubscriptionDTO userNotificationSubscriptionDTO) {
		log.info("Notification User Subscription Controller: editSubscription ");
		return userNotificationSubscriptionService.createUpdateUserNotificationSubscription(userNotificationSubscriptionDTO);

	}


}