package ru.practicum.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

    public ValidationException(HttpStatus badRequest, final String message) {
        super(badRequest, message);
    }
}
