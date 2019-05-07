package com.jbhunt.infrastructure.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.hrms.eoi.api.dto.person.PersonDTO;
import com.jbhunt.infrastructure.notification.service.EOIHelperService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EOIController {

	private final EOIHelperService eoiHelperService;

	/**
	 * Constructor for EOIHelperService
	 * 
	 * @param eoiHelperService
	 */
	public EOIController(EOIHelperService eoiHelperService) {
		this.eoiHelperService = eoiHelperService;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping(value="/fetchinternaluserdetails/{userId}")
	public ResponseEntity<PersonDTO> fetchEmployeeDetail(@PathVariable String userId) {
		log.debug("EOIController :: fetchEmployeeDetail");
		PersonDTO personDTO = eoiHelperService.fetchEmployeeDetailForUserId(userId);
		return ResponseEntity.ok(personDTO);
	}

}
