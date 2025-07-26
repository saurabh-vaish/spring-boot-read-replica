package com.app.readreplica.controller;

import com.app.readreplica.models.Token;
import com.app.readreplica.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Token operations
 * 
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@Log4j2
@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<Token> createToken() {
        log.info("Received request to create new token");
        Token token = tokenService.createToken();
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Token> getTokenById(@PathVariable Long id) {
        log.info("Received request to get token with id: {}", id);
        Optional<Token> token = tokenService.findById(id);
        return token.map(t -> ResponseEntity.ok(t))
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Token>> getAllTokens() {
        log.info("Received request to get all tokens");
        List<Token> tokens = tokenService.findAll();
        return ResponseEntity.ok(tokens);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Token> updateToken(@PathVariable Long id, 
                                           @RequestParam(required = false) String newValue) {
        log.info("Received request to update token with id: {} and new value: {}", id, 
                 newValue != null ? "provided" : "auto-generated");
        Token updatedToken = tokenService.updateToken(id, newValue);
        return ResponseEntity.ok(updatedToken);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToken(@PathVariable Long id) {
        log.info("Received request to delete token with id: {}", id);
        tokenService.deleteToken(id);
        return ResponseEntity.noContent().build();
    }
}
