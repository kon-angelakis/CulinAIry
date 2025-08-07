package com.kangel.thesis.aipowered_location_advisor.Config.Exceptions;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MissingRequestFieldException.class)
    public ResponseEntity<?> handleMissingField(MissingRequestFieldException ex) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", ex.getMessage()
        ));
    }

}
