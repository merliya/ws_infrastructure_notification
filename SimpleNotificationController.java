package com.jbhunt.infrastructure.notification.controller;

import com.jbhunt.infrastructure.notification.service.NotificationDeliveryService;
import com.jbhunt.infrastructure.notification.unifiedschema.dto.NotificationDeliveryChannel;
import com.jbhunt.infrastructure.notification.unifiedschema.dto.NotificationSubscriptionInAppDTO;
import com.jbhunt.infrastructure.notification.unifiedschema.dto.SimpleNotification;
import com.jbhunt.infrastructure.notification.unifiedschema.dto.UnifiedNotificationNonRecurringEvent;
import com.jbhunt.infrastructure.notification.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * SimpleNotificationController provides an easy to use service for delivering
 * in-app notifications.
 *
 * @author jisabn4
 *
 */
@RestController
@Slf4j
public class SimpleNotificationController {

    private final NotificationDeliveryService userNotificationDeliveryService;

    public SimpleNotificationController( NotificationDeliveryService userNotificationDeliveryService) {
        this.userNotificationDeliveryService = userNotificationDeliveryService;
    }

    @PostMapping(value = "/simplenotification")
    public ResponseEntity<String> createSimpleNotification(@Valid @RequestBody SimpleNotification simpleNotification) throws Exception {
        NotificationSubscriptionInAppDTO notificationSubscriptionInAppDTO = new NotificationSubscriptionInAppDTO();
        notificationSubscriptionInAppDTO.setNotificationType(simpleNotification.getType());
        notificationSubscriptionInAppDTO.setNotificationContent(simpleNotification.getContent());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(simpleNotification.getUserid());

        UnifiedNotificationNonRecurringEvent unifiedNotificationNonRecurringEvent = new UnifiedNotificationNonRecurringEvent();
        unifiedNotificationNonRecurringEvent.setNotificationDeliveryChannel(NotificationDeliveryChannel.INAPP);
        unifiedNotificationNonRecurringEvent.setNotificationSubscriptionInAppContent(notificationSubscriptionInAppDTO);
        unifiedNotificationNonRecurringEvent.setSubscriberUser(userDTO);
        unifiedNotificationNonRecurringEvent.setApplicationDomainCode(simpleNotification.getApplication());

        userNotificationDeliveryService.deliverNotification(unifiedNotificationNonRecurringEvent);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
