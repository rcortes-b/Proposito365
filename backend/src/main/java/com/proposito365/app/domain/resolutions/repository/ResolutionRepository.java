package com.proposito365.app.domain.resolutions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.domain.resolutions.domain.Resolution;

public interface ResolutionRepository extends JpaRepository<Resolution, Long> {}
