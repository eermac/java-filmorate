package ru.yandex.practicum.filmorate.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends RuntimeException {
    private final static Logger log = LoggerFactory.getLogger(ValidationException.class);
    public ValidationException(final String message){
        log.error(message);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}


