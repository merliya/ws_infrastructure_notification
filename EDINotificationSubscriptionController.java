package com.jbhunt.infrastructure.notification.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationEventMappingDTO;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationSubscriptionDTO;
import com.jbhunt.infrastructure.notification.entity.EDINotificationSubscription;
import com.jbhunt.infrastructure.notification.service.EDINotificationSubscriptionService;

import lombok.extern.slf4j.Slf4j;

/**
 * EDINotificationUserSubscriptionController
 * 
 * @author
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "/subscriptions/edisubscriptions")
public class EDINotificationSubscriptionController {

    private EDINotificationSubscriptionService ediNotificationSubscriptionService;

    public EDINotificationSubscriptionController(
            EDINotificationSubscriptionService ediNotificationSubscriptionService) {
        this.ediNotificationSubscriptionService = ediNotificationSubscriptionService;
    }

    @GetMapping(value = "/{subscriptionID}")
    public ResponseEntity<EDINotificationSubscriptionDTO> getEDISubscriptions(@PathVariable Integer subscriptionID) throws JsonParseException {
        log.info("Notification EDI Subscription Controller: Get EDI Subscription Details ");
        EDINotificationSubscriptionDTO ediNotificationSubscriptionDTO = ediNotificationSubscriptionService
                .getEDINotificationSubsciption(subscriptionID);
        return Optional.ofNullable(ediNotificationSubscriptionDTO).isPresent()
                ? new ResponseEntity<>(ediNotificationSubscriptionDTO, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EDINotificationSubscriptionDTO> createEDISubscriptionNotification(
            @RequestBody EDINotificationSubscriptionDTO subscriptionDTO) {
        log.info("Notification EDI Subscription Controller: createEDISubscriptionNotification ");
        EDINotificationSubscription entity = ediNotificationSubscriptionService.createOrUpdateEDINotificationSubscription(subscriptionDTO);
        subscriptionDTO.setShipmentNotificationSubscriptionID(entity.getEdiNotificationSubscriptionID());
        return ResponseEntity.ok(subscriptionDTO);
    }

    @PutMapping
    public ResponseEntity<EDINotificationSubscriptionDTO> editEDISubscriptionNotification(
            @RequestBody EDINotificationSubscriptionDTO subscriptionDTO) {
        log.info("Notification EDI Subscription Controller: editEDISubscriptionNotification ");
        ediNotificationSubscriptionService.createOrUpdateEDINotificationSubscription(subscriptionDTO); 
        return ResponseEntity.ok(subscriptionDTO);
    }

    @PatchMapping(value = "/{ediSubscriptionType}/{statusType}/{subscriptionID}")
    public ResponseEntity<EDINotificationSubscriptionDTO> activateOrInactivateSubscription(@PathVariable String ediSubscriptionType,
            @PathVariable String statusType, @PathVariable Integer subscriptionID) {
        log.info("Notification EDI Subscription Controller: EDISubscription Type " + ediSubscriptionType
                + ",StatusType " + statusType + "Subscription ID " + subscriptionID);
        EDINotificationSubscriptionDTO subscriptionDTO = ediNotificationSubscriptionService
                .updateEDINotificationSubscriptionStatus(ediSubscriptionType, statusType, subscriptionID);
        return ResponseEntity.ok(subscriptionDTO);
    }
    
    @GetMapping(value = "eventMapping")
    public ResponseEntity<List<EDINotificationEventMappingDTO>> getEDIEventMapping(@RequestParam(value = "eventTypeCode") String eventTypeCode) {
        log.info("Notification EDI Subscription Controller: Get EDI Event mapping Details ");
        List<EDINotificationEventMappingDTO> ediNotificationEventMappingDTO = ediNotificationSubscriptionService
                .getEDINotificationEventMapping(eventTypeCode);
        return ResponseEntity.ok(ediNotificationEventMappingDTO);
    
}
}
