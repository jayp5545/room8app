package com.ASDC.backend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling exceptions across the whole application.
 */
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LogManager.getLogger(ControllerAdvice.class);

    /**
     * Handles validation exceptions.
     *
     * @param ex the MethodArgumentNotValidException
     * @return the response entity with error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            logger.warn("Validation error on field: {} - {}", error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom exceptions.
     *
     * @param exception the CustomException
     * @return the response entity with error code and message
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> handleCustomException(CustomException exception) {
        CustomExceptionResponse response = new CustomExceptionResponse(exception.getErrorCode(), exception.getErrorMessage());
        logger.error("Custom exception: {} - {}", exception.getErrorCode(), exception.getErrorMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles missing servlet request parameter exceptions.
     *
     * @param exception the MissingServletRequestParameterException
     * @return the response entity with error code and message
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomExceptionResponse> handleMissingParams(MissingServletRequestParameterException exception) {
        String name = exception.getParameterName();
        CustomExceptionResponse response = new CustomExceptionResponse("400", "Missing required parameter: " + name);
        logger.warn("Missing request parameter: {}", name);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles method argument type mismatch exceptions.
     *
     * @param exception the MethodArgumentTypeMismatchException
     * @return the response entity with error code and message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomExceptionResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String name = exception.getName();
        CustomExceptionResponse response = new CustomExceptionResponse("400", "Invalid value for parameter: " + name);
        logger.warn("Type mismatch for parameter: {}", name);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles expired JWT exceptions.
     *
     * @param exception the ExpiredJwtException
     * @return the response entity with error code and message
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CustomExceptionResponse> handleExpiredJwt(ExpiredJwtException exception) {
        CustomExceptionResponse response = new CustomExceptionResponse("401", exception.getMessage());
        logger.error("Expired JWT: {}", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles no handler found exceptions.
     *
     * @param ex the NoHandlerFoundException
     * @return the response entity with error code and message
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        CustomExceptionResponse response = new CustomExceptionResponse("404", "The requested endpoint " + ex.getRequestURL() + " was not found.");
        logger.warn("No handler found for request: {}", ex.getRequestURL());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}