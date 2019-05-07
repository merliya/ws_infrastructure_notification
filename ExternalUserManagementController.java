package com.jbhunt.infrastructure.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.infrastructure.notification.service.ExternalUserManagementService;

/**
 * ExternalUserManagementController provides a REST service to check if the
 * given user is external user.
 * 
 * @author rcon335
 *
 */
@RestController
public class ExternalUserManagementController {

	private final ExternalUserManagementService externalUserManagementService;

	/**
	 * Constructor for ExternalUserManagementController.
	 * 
	 * @param externalUserManagementService
	 */
	public ExternalUserManagementController(ExternalUserManagementService externalUserManagementService) {
		this.externalUserManagementService = externalUserManagementService;
	}

	/**
	 * getExternalUserDetails method checks if the given user is external user.
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/getcontacttype/{userId}")
	public Boolean getExternalUserDetails(@PathVariable("userId") String userId) {
		return externalUserManagementService.checkExternalUserManagementService(userId);
	}

}
