package ru.yandex.practicum.filmorate.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends RuntimeException {
    private final static Logger log = LoggerFactory.getLogger(ValidationException.class);
    public ValidationException(final String message, HttpMethod method){
        log.error(message);

        if(HttpMethod.PUT.equals(method)){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (HttpMethod.POST.equals(method)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}


