package com.proposito365.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proposito365.app.models.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {
	Optional<Roles> findByRole(String role);
}
