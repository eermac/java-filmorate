package ru.yandex.practicum.filmorate.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationException extends IOException {
    private final static Logger log = LoggerFactory.getLogger(ValidationException.class);
    public ValidationException(final String message){
        super(message);
        log.error(message);
    }
}
