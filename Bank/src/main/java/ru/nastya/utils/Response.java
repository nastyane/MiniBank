package ru.nastya.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.nastya.api.FailResponse;
import ru.nastya.exception.NotExistsException;

public class Response {
    public static ResponseEntity<FailResponse> notFound(NotExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponse(e.getMessage()));
    }
}
