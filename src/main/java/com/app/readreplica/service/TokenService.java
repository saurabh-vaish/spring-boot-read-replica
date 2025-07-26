package com.app.readreplica.service;

import com.app.readreplica.config.database.annotations.ForceMaster;
import com.app.readreplica.config.database.annotations.ReadOnlyReplica;
import com.app.readreplica.config.database.annotations.WriteTransaction;
import com.app.readreplica.exception.TokenNotFoundException;
import com.app.readreplica.models.Token;
import com.app.readreplica.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

/**
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@RequiredArgsConstructor
@Log4j2
@Service
//@Transactional
public class TokenService {

    private final TokenRepository tokenRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 32;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public Token createToken() {
        log.info("Starting token creation process");
        String tokenValue = generateRandomToken();
        Token token = new Token();
        token.setToken(tokenValue);
        log.info("Creating new token with value: {}", tokenValue);

        Token savedToken = tokenRepository.save(token);
        log.info("Successfully created token with id: {} and value: {}", savedToken.getId(), tokenValue);
        return savedToken;
    }

//    @ReadOnlyReplica
    @ForceMaster
    @Transactional(readOnly = true)
    public Optional<Token> findById(Long id) {
        log.info("Starting search for token with id: {}", id);
        Optional<Token> result = tokenRepository.findById(id);
        if (result.isPresent()) {
            log.info("Successfully found token with id: {}", id);
            log.debug("Token details - id: {}, value: {}", result.get().getId(), result.get().getToken());
        } else {
            log.warn("No token found with id: {}", id);
        }
        return result;
    }

    @ReadOnlyReplica
    @Transactional(readOnly = true)
    public List<Token> findAll() {
        log.info("Starting retrieval of all tokens");
        List<Token> tokens = tokenRepository.findAll();
//        Thread.sleep(1000);
        log.info("Successfully retrieved {} tokens", tokens.size());
        log.debug("Token count details: {}", tokens.size());
        return tokens;
    }

    @Transactional
    public Token updateToken(Long id, String newTokenValue) {
        log.info("Starting token update for id: {} with new value: {}", id,
                 newTokenValue != null ? "provided" : "auto-generated");

        Optional<Token> tokenOpt = tokenRepository.findById(id);
        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();
            String oldValue = token.getToken();
            String finalTokenValue = newTokenValue != null ? newTokenValue : generateRandomToken();
            token.setToken(finalTokenValue);

            log.info("Updating token id: {} - old value: {}, new value: {}", id, oldValue, finalTokenValue);
            Token updatedToken = tokenRepository.save(token);
            log.info("Successfully updated token with id: {}", id);
            return updatedToken;
        } else {
            log.error("Token not found with id: {} for update operation", id);
            throw new TokenNotFoundException(id);
        }
    }

//    @Transactional
    public void deleteToken(Long id) {
        log.info("Starting deletion of token with id: {}", id);
        // Check if token exists before deletion
        Optional<Token> existingToken = tokenRepository.findById(id);
        if (existingToken.isPresent()) {
            log.info("Token found with id: {}, proceeding with deletion", id);
            tokenRepository.deleteById(id);
            log.info("Successfully deleted token with id: {}", id);
        } else {
            log.warn("Attempted to delete non-existent token with id: {}", id);
            // Note: Spring's deleteById doesn't throw exception for non-existent entities
            // but we log it as a warning for monitoring purposes
        }
    }

    private String generateRandomToken() {
        log.debug("Generating random token with length: {}", TOKEN_LENGTH);
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        String generatedToken = token.toString();
        log.debug("Successfully generated random token");
        return generatedToken;
    }
}
