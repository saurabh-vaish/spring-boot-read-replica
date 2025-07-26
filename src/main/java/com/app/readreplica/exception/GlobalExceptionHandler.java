package com.app.readreplica.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;

/**
 * Global exception handler for the application
 * 
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTokenNotFoundException(
            TokenNotFoundException ex, WebRequest request) {

        log.error("Token not found: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );
        problemDetail.setTitle("Token Not Found");
        problemDetail.setType(URI.create("https://api.readreplica.com/problems/token-not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", extractPath(request));

        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenServiceException.class)
    public ResponseEntity<ProblemDetail> handleTokenServiceException(
            TokenServiceException ex, WebRequest request) {

        log.error("Token service error: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage()
        );
        problemDetail.setTitle("Token Service Error");
        problemDetail.setType(URI.create("https://api.readreplica.com/problems/token-service-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", extractPath(request));

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ProblemDetail> handleDataAccessException(
            DataAccessException ex, WebRequest request) {

        log.error("Database access error: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An error occurred while accessing the database"
        );
        problemDetail.setTitle("Database Error");
        problemDetail.setType(URI.create("https://api.readreplica.com/problems/database-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", extractPath(request));
        problemDetail.setProperty("errorType", ex.getClass().getSimpleName());

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ProblemDetail> handleTransactionException(
            TransactionException ex, WebRequest request) {

        log.error("Transaction error: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An error occurred during transaction processing"
        );
        problemDetail.setTitle("Transaction Error");
        problemDetail.setType(URI.create("https://api.readreplica.com/problems/transaction-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", extractPath(request));
        problemDetail.setProperty("errorType", ex.getClass().getSimpleName());

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        log.error("Invalid argument: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        problemDetail.setTitle("Invalid Argument");
        problemDetail.setType(URI.create("https://api.readreplica.com/problems/invalid-argument"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", extractPath(request));

        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex, WebRequest request) {

        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred"
        );
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.readreplica.com/problems/internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", extractPath(request));
        problemDetail.setProperty("errorType", ex.getClass().getSimpleName());

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Extract the path from WebRequest description
     */
    private String extractPath(WebRequest request) {
        String description = request.getDescription(false);
        return description.replace("uri=", "");
    }
}
