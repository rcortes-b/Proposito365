package com.proposito365.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {}