package com.hafizbahtiar.murmur.features.users.services;

import com.hafizbahtiar.murmur.features.users.dto.UserRegistrationRequest;
import com.hafizbahtiar.murmur.features.users.dto.UserResponse;
import com.hafizbahtiar.murmur.features.users.entities.User;
import com.hafizbahtiar.murmur.features.users.exceptions.UserAlreadyExistsException;
import com.hafizbahtiar.murmur.features.users.exceptions.UserNotFoundException;
import com.hafizbahtiar.murmur.features.users.mappers.UserMapper;
import com.hafizbahtiar.murmur.features.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse register(UserRegistrationRequest request) {
        try {
            if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
                throw UserAlreadyExistsException.email(request.getEmail());
            }

            if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
                throw UserAlreadyExistsException.username(request.getUsername());
            }

//            User user = userMapper.toEntity(request);

//            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

//            User savedUser = userRepository.saveAndFlush(user);

//            UserResponse response = userMapper.toResponse(savedUser);

            return null;
        } catch (UserAlreadyExistsException e) {
            // Re-throw user already exists exceptions
            throw e;
        } catch (Exception e) {
//            log.error("Unexpected error during user registration for email: {}", request.getEmail(), e);
            throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.byId(id));
//        return userMapper.toResponse(user);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> UserNotFoundException.byUsername(username));
//        return userMapper.toResponse(user);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findByActiveTrue();
//        return userMapper.toResponseList(users);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }
}
