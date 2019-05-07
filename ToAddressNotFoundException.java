package com.jbhunt.infrastructure.notification.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="To Email Address Not Found")
public class ToAddressNotFoundException extends RuntimeException {
    public ToAddressNotFoundException(String s) {
        super(s);
        log.error(s);
    }
}