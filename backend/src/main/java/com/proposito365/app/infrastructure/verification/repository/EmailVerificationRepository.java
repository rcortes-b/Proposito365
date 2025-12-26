package com.proposito365.app.infrastructure.verification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.infrastructure.verification.domain.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
	Optional<EmailVerification> findByEmail(String email);
}
