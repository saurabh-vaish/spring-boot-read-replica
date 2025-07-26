package com.app.readreplica.exception;

/**
 * Exception thrown when a token is not found
 * 
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
public class TokenNotFoundException extends RuntimeException {
    
    public TokenNotFoundException(String message) {
        super(message);
    }
    
    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TokenNotFoundException(Long tokenId) {
        super("Token not found with id: " + tokenId);
    }
}
