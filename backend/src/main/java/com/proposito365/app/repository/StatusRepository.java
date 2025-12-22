package com.proposito365.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
	Optional<Status> findByValue(String value);
}