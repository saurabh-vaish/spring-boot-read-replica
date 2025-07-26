package com.app.readreplica.exception;

/**
 * General exception for token service operations
 * 
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
public class TokenServiceException extends RuntimeException {
    
    public TokenServiceException(String message) {
        super(message);
    }
    
    public TokenServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
