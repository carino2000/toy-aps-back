package org.example.toyaps.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalAccessException.class)
    public ResponseEntity<?> handleIllegalAccessException(IllegalAccessException e) {
        Map<String,Object> resp = Map.of("message", "Please run the simulate first.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }
}
