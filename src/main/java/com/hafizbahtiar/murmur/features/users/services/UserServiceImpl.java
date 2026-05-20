package com.hafizbahtiar.murmur.features.users.services;

import com.hafizbahtiar.murmur.features.users.dto.*;
import com.hafizbahtiar.murmur.features.users.entities.User;
import com.hafizbahtiar.murmur.features.users.exceptions.*;
import com.hafizbahtiar.murmur.features.users.mappers.UserMapper;
import com.hafizbahtiar.murmur.features.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw UserAlreadyExistsException.email(request.getEmail());
        }
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw UserAlreadyExistsException.username(request.getUsername());
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        try {
            return userMapper.toResponse(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException e) {
            // Concurrent registration slipped past the existence checks above
            if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
                throw UserAlreadyExistsException.email(request.getEmail());
            }
            if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
                throw UserAlreadyExistsException.username(request.getUsername());
            }
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userMapper.toResponse(
                userRepository.findById(id).orElseThrow(() -> UserNotFoundException.byId(id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUuid(UUID uuid) {
        return userMapper.toResponse(
                userRepository.findByUuid(uuid).orElseThrow(() -> UserNotFoundException.byUuid(uuid))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        return userMapper.toResponse(
                userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> UserNotFoundException.byEmail(email))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        return userMapper.toResponse(
                userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> UserNotFoundException.byUsername(username))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findByActiveTrue(pageable).map(userMapper::toResponse);
    }

    @Override
    @Transactional
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
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
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
    @Transactional
    public UserResponse updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.byId(userId));

        String upperNewRole = newRole != null ? newRole.toUpperCase() : null;

        if (upperNewRole == null
                || (!upperNewRole.equals("USER") && !upperNewRole.equals("OWNER") && !upperNewRole.equals("ADMIN"))) {
            throw UserRoleException.invalidRole(newRole);
        }

        String currentRole = user.getRole();

        if ("OWNER".equals(upperNewRole) && !"OWNER".equalsIgnoreCase(currentRole)) {
            if (userRepository.existsOwner()) {
                throw UserRoleException.ownerAlreadyExists();
            }
        }

        if ("OWNER".equalsIgnoreCase(currentRole) && !"OWNER".equals(upperNewRole)) {
            // This user IS the owner; prevent demotion
            throw UserRoleException.cannotRemoveOwner();
        }

        user.setRole(upperNewRole);
        return userMapper.toResponse(userRepository.save(user));
    }
}
