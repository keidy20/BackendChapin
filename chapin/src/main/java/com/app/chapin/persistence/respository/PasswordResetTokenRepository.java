package com.app.chapin.persistence.respository;

import com.app.chapin.persistence.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    public Optional<PasswordResetToken> findByToken(String token);
}
