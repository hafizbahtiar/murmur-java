package com.hafizbahtiar.murmur.features.users.controllers;

import com.hafizbahtiar.murmur.features.users.repositories.UserRepository;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hafizbahtiar.murmur.features.users.entities.User;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
