package com.proposito365.app.domain.groups.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.domain.groups.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {}
