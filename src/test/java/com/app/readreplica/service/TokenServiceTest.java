package com.app.readreplica.service;

import com.app.readreplica.exception.TokenNotFoundException;
import com.app.readreplica.models.Token;
import com.app.readreplica.repositories.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TokenService
 * 
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    private Token testToken;

    @BeforeEach
    void setUp() {
        testToken = new Token();
        testToken.setId(1L);
        testToken.setToken("testToken123456789012345678901234");
        testToken.setExpired(false);
        testToken.setRevoked(false);
        testToken.setVerified(false);
    }

    @Test
    void createToken_ShouldCreateAndReturnNewToken() {
        // Given
        Token savedToken = new Token();
        savedToken.setId(1L);
        savedToken.setToken("generatedToken123456789012345678");
        
        when(tokenRepository.save(any(Token.class))).thenReturn(savedToken);

        // When
        Token result = tokenService.createToken();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getToken()).hasSize(32); // TOKEN_LENGTH = 32
        
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void findById_ShouldReturnTokenWhenExists() {
        // Given
        Long tokenId = 1L;
        when(tokenRepository.findById(tokenId)).thenReturn(Optional.of(testToken));

        // When
        Optional<Token> result = tokenService.findById(tokenId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(tokenId);
        assertThat(result.get().getToken()).isEqualTo("testToken123456789012345678901234");
        
        verify(tokenRepository, times(1)).findById(tokenId);
    }

    @Test
    void findAll_ShouldReturnAllTokens() {
        // Given
        Token token2 = new Token();
        token2.setId(2L);
        token2.setToken("anotherToken12345678901234567890");
        
        List<Token> tokens = Arrays.asList(testToken, token2);
        when(tokenRepository.findAll()).thenReturn(tokens);

        // When
        List<Token> result = tokenService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testToken, token2);
        
        verify(tokenRepository, times(1)).findAll();
    }

    @Test
    void updateToken_WithNewValue_ShouldUpdateAndReturnToken() {
        // Given
        Long tokenId = 1L;
        String newTokenValue = "newTokenValue123456789012345678";
        Token updatedToken = new Token();
        updatedToken.setId(tokenId);
        updatedToken.setToken(newTokenValue);
        
        when(tokenRepository.findById(tokenId)).thenReturn(Optional.of(testToken));
        when(tokenRepository.save(any(Token.class))).thenReturn(updatedToken);

        // When
        Token result = tokenService.updateToken(tokenId, newTokenValue);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(tokenId);
        assertThat(result.getToken()).isEqualTo(newTokenValue);
        
        verify(tokenRepository, times(1)).findById(tokenId);
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void updateToken_WithNullValue_ShouldGenerateAndUpdateToken() {
        // Given
        Long tokenId = 1L;
        Token updatedToken = new Token();
        updatedToken.setId(tokenId);
        updatedToken.setToken("generatedToken123456789012345678");
        
        when(tokenRepository.findById(tokenId)).thenReturn(Optional.of(testToken));
        when(tokenRepository.save(any(Token.class))).thenReturn(updatedToken);

        // When
        Token result = tokenService.updateToken(tokenId, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(tokenId);
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getToken()).hasSize(32); // Auto-generated token should be 32 characters
        
        verify(tokenRepository, times(1)).findById(tokenId);
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void deleteToken_WithExistingToken_ShouldDeleteSuccessfully() {
        // Given
        Long tokenId = 1L;
        when(tokenRepository.findById(tokenId)).thenReturn(Optional.of(testToken));

        // When
        tokenService.deleteToken(tokenId);

        // Then
        verify(tokenRepository, times(1)).findById(tokenId);
        verify(tokenRepository, times(1)).deleteById(tokenId);
    }

    @Test
    void findById_WithNonExistentToken_ShouldReturnEmpty() {
        // Given
        Long nonExistentId = 999L;
        when(tokenRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        Optional<Token> result = tokenService.findById(nonExistentId);

        // Then
        assertThat(result).isEmpty();
        
        verify(tokenRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void updateToken_WithNonExistentToken_ShouldThrowTokenNotFoundException() {
        // Given
        Long nonExistentId = 999L;
        String newTokenValue = "newValue";
        when(tokenRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> tokenService.updateToken(nonExistentId, newTokenValue))
                .isInstanceOf(TokenNotFoundException.class)
                .hasMessage("Token not found with id: " + nonExistentId);
        
        verify(tokenRepository, times(1)).findById(nonExistentId);
        verify(tokenRepository, never()).save(any(Token.class));
    }

    @Test
    void deleteToken_WithNonExistentToken_ShouldHandleGracefully() {
        // Given
        Long nonExistentId = 999L;
        when(tokenRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        tokenService.deleteToken(nonExistentId);

        // Then
        verify(tokenRepository, times(1)).findById(nonExistentId);
        verify(tokenRepository, never()).deleteById(anyLong());
    }

    @Test
    void createToken_ShouldGenerateRandomTokenWith32Characters() {
        // Given
        Token savedToken = new Token();
        savedToken.setId(1L);
        
        when(tokenRepository.save(any(Token.class))).thenAnswer(invocation -> {
            Token token = invocation.getArgument(0);
            savedToken.setToken(token.getToken());
            return savedToken;
        });

        // When
        Token result = tokenService.createToken();

        // Then
        assertThat(result.getToken()).isNotNull();
        assertThat(result.getToken()).hasSize(32);
        assertThat(result.getToken()).matches("[A-Za-z0-9]{32}");
        
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void findAll_WithEmptyRepository_ShouldReturnEmptyList() {
        // Given
        when(tokenRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Token> result = tokenService.findAll();

        // Then
        assertThat(result).isEmpty();
        
        verify(tokenRepository, times(1)).findAll();
    }
}