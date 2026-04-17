package com.hafizbahtiar.murmur.features.users.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hafizbahtiar.murmur.features.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
}
