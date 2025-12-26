package com.proposito365.app.domain.status;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
	Optional<Status> findByValue(String value);
}