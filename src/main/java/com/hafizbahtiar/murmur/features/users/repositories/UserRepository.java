package com.hafizbahtiar.murmur.features.users.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hafizbahtiar.murmur.features.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByActiveTrue();

    List<User> findByRoleAndActiveTrue(String role);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);
}
