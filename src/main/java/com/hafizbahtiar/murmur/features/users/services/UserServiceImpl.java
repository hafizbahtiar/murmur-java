package com.hafizbahtiar.murmur.features.users.services;

import com.hafizbahtiar.murmur.features.users.dto.*;
import com.hafizbahtiar.murmur.features.users.entities.User;
import com.hafizbahtiar.murmur.features.users.exceptions.*;
import com.hafizbahtiar.murmur.features.users.mappers.UserMapper;
import com.hafizbahtiar.murmur.features.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserRegistrationRequest request) {
        log.info("Register attempt started for email={} username={}",
                request.getEmail(), request.getUsername());

        try {
            // Check email
            log.debug("Checking if email already exists: {}", request.getEmail());
            if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
                log.warn("Registration failed - email already exists: {}", request.getEmail());
                throw UserAlreadyExistsException.email(request.getEmail());
            }

            // Check username
            log.debug("Checking if username already exists: {}", request.getUsername());
            if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
                log.warn("Registration failed - username already exists: {}", request.getUsername());
                throw UserAlreadyExistsException.username(request.getUsername());
            }

            // Mapping
            log.debug("Mapping UserRegistrationRequest to User entity");
            User user = userMapper.toEntity(request);

            // Password encoding
            log.debug("Encoding password for email={}", request.getEmail());
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

            // Save
            log.debug("Saving user to database");
            User savedUser = userRepository.saveAndFlush(user);

            log.info("User successfully registered with id={} email={}",
                    savedUser.getId(), savedUser.getEmail());

            // Mapping response
            log.debug("Mapping saved User to UserResponse");
            return userMapper.toResponse(savedUser);

        } catch (UserAlreadyExistsException e) {
            log.warn("Registration rejected: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Unexpected error during registration for email={}",
                    request.getEmail(), e);
            throw new RuntimeException("Failed to register user", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.byId(id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUuid(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> UserNotFoundException.byUuid(uuid));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> UserNotFoundException.byEmail(email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> UserNotFoundException.byUsername(username));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findByActiveTrue();
        return userMapper.toResponseList(users);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.byId(id));

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw UserAlreadyExistsException.email(request.getEmail());
            }
        }

        if (request.getUsername() != null && !request.getUsername().equalsIgnoreCase(user.getUsername())) {
            if (userRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
                throw UserAlreadyExistsException.username(request.getUsername());
            }
        }

        userMapper.updateEntityFromRequest(request, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> UserNotFoundException.byId(id));

        user.deactivate();

        userRepository.save(user);
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

    @Override
    public UserResponse updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.byId(userId));

        String currentRole = user.getRole();
        String upperNewRole = newRole != null ? newRole.toUpperCase() : null;

        if (
                upperNewRole == null
                        || (!upperNewRole.equals("USER")
                        && !upperNewRole.equals("OWNER")
                        && !upperNewRole.equals("ADMIN"))
        ) {
            throw UserRoleException.invalidRole(newRole);
        }

        if ("OWNER".equals(upperNewRole) && !"OWNER".equalsIgnoreCase(currentRole)) {
            if (userRepository.existsOwner()) {
                throw UserRoleException.ownerAlreadyExists();
            }
        }

        if ("OWNER".equalsIgnoreCase(currentRole) && !"OWNER".equals(upperNewRole)) {
            Optional<User> existingOwner = userRepository.findOwner();
            if (existingOwner.isPresent() && existingOwner.get().getId().equals(userId)) {
                throw UserRoleException.cannotRemoveOwner();
            }
        }

        user.setRole(upperNewRole);
        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }
}
