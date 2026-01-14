package com.blog01.backend.common.exception;

import com.blog01.backend.common.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseData<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ResponseData.error("Request method '" + ex.getMethod() + "' not supported"));
    }

    // 2. Handle 403 Forbidden (Security Block)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseData<Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseData.error("You do not have permission to access this resource"));
    }

    // 3. Handle Login Errors (Bad Credentials)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseData<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseData.error("Invalid email or password"));
    }

    // 4. Handle Any Other Unexpected Error (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<Object>> handleGeneralException(Exception ex) {
        ex.printStackTrace(); // Print log for you to debug
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseData.error("An unexpected error occurred: " + ex.getMessage()));
    }
}