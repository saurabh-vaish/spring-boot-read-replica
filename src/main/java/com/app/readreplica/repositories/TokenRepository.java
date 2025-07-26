package com.app.readreplica.repositories;

import com.app.readreplica.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}