package com.proposito365.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.Roles;

public interface RoleRepositoryInterface extends JpaRepository<Roles, Long> {}
