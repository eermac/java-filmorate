package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(final String message, HttpMethod method){
        log.error(message);

        if(HttpMethod.PUT.equals(method)){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (HttpMethod.POST.equals(method)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}


