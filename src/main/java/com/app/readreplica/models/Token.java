package com.app.readreplica.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "expired", nullable = false)
    private Boolean expired = false;

    @Column(name = "last_accessed")
    private Instant lastAccessed;

    @Column(name = "revoked", nullable = false)
    private Boolean revoked = false;

    @Column(name = "login_time")
    private Instant loginTime;

    @Column(name = "token")
    private String token;

    @Column(name = "token_type")
    private String tokenType;

    @ColumnDefault("false")
    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

}