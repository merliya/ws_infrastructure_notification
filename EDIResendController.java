package com.jbhunt.infrastructure.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.infrastructure.edinotification.subscription.dto.EDINotificationResendDTO;
import com.jbhunt.infrastructure.edinotification.subscription.dto.EDIResendInputParameterDTO;
import com.jbhunt.infrastructure.notification.service.EDIResendService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/edinotification/resend")
public class EDIResendController {
	
	private final EDIResendService ediResendService;
	
	public EDIResendController(
			EDIResendService ediResendService) {
        this.ediResendService = ediResendService;
    }



	@PostMapping
	public ResponseEntity<List<EDINotificationResendDTO>> getEDIResendDetails(
			@RequestParam(value = "orderId", required = true) String orderId,
			@RequestBody(required = false) EDIResendInputParameterDTO ediResendDTO) {
		List<EDINotificationResendDTO> notificationLogResponse = ediResendService
                .getEDIResendDetails(orderId,ediResendDTO);
		
        return ResponseEntity.ok(notificationLogResponse);
	}
	
	
}
