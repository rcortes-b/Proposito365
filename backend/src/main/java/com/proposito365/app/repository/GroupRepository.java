package com.proposito365.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {}
