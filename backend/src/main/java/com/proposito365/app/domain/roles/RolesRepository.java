package com.proposito365.app.domain.roles;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long> {
	Optional<Roles> findByRole(String role);
}
