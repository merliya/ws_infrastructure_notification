package com.jbhunt.infrastructure.notification.controller;

import java.util.List;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jbhunt.infrastructure.notification.entity.NotificationLog;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationSubscriptionDTO;
import com.jbhunt.infrastructure.notification.service.NotificationLogService;
import com.jbhunt.infrastructure.usernotification.subscription.dto.NotificationLogContentDTO;
import com.jbhunt.infrastructure.usernotification.subscription.dto.NotificationLogRawContentDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * NotificationLogController provides service to perform update operation on
 * notification log table.
 * 
 * @author rcon335
 *
 */
@RepositoryRestController
@RequestMapping(value = "/usernotificationlog")
@Slf4j
public class NotificationLogController {

	private NotificationLogService notificationLogService;

	/**
	 * Constructor for NotificationLogController
	 * 
	 * @param notificationLogService
	 */
	public NotificationLogController(NotificationLogService notificationLogService) {
		this.notificationLogService = notificationLogService;
	}

	/**
	 * updateReadReceiptTimestamp method updates the read receipt timestamp in
	 * notification log table to indicate that the in-app notification is read
	 * by the user.
	 * 
	 * @param notificationLogID
	 * @return
	 */
	@PatchMapping("/updatetimestamp/{notificationLogID}")
	public ResponseEntity<NotificationLog> updateReadReceiptTimestamp(@PathVariable Integer notificationLogID) {
		log.debug("Updating Read Receipt timestamp");
		NotificationLog notificationLog = notificationLogService.updateReadReceiptTimestamp(notificationLogID);
		return ResponseEntity.ok(notificationLog);
	}
	
    @GetMapping(value = "/getNotificationLog/{notificationLogID}")
    public ResponseEntity<NotificationLogContentDTO> getNotificationLog(@PathVariable Integer notificationLogID) {
        log.info("Notification Log Controller: Get Notification Raw Log Content ");
        List<NotificationLogRawContentDTO> notificationLogRawContentDTOs = notificationLogService
                .getRawNotificationLogContent(notificationLogID);
        NotificationLogContentDTO notificationLogContentDTO = new NotificationLogContentDTO();
        notificationLogContentDTO.setNotificationRawData(notificationLogRawContentDTOs);
        return ResponseEntity.ok(notificationLogContentDTO);
    }
    
    @GetMapping(value = "/notificationRawData/{notificationRawLogId}")
    public ResponseEntity<NotificationLogRawContentDTO> getNotificationRawLog(@PathVariable Integer notificationRawLogId){
        return ResponseEntity.ok(notificationLogService.getRawNotificationContentByRawId(notificationRawLogId));
    }

}
