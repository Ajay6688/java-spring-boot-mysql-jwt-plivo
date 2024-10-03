package com.hc.comms.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Global exception handler class
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request , String message ) {
        logger.error("[SmsApiController][sendMessages] " + ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", false);
        body.put("message",  message != null ? message : "Illegal argument encountered!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Handle null pointer exception
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request, String message) {
        logger.error("[SmsApiController][sendMessages] Null Pointer Exception: " + ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", false);
        body.put("message", message != null ? message : "Null data encountered!");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        logger.error("[SmsApiController][sendMessages] General Exception: " + ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", false);
        body.put("message", "An unexpected error occurred");
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex , WebRequest request , String message){
//        logger.error("[SmsApiController][sendMessages] UsernameNotFoundException Exception: " + ex.getMessage());
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", false);
//        body.put("message", "Client Id not found");
//        body.put("details", ex.getMessage());
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }

}
