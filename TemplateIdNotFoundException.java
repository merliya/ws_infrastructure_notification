package com.jbhunt.infrastructure.notification.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Template ID not found")
public class TemplateIdNotFoundException extends RuntimeException {
    public TemplateIdNotFoundException(String s) {
        super(s);
        log.error(s);
    }
}
