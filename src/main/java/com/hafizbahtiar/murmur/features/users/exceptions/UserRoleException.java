package com.hafizbahtiar.murmur.features.users.exceptions;

import com.hafizbahtiar.murmur.common.exceptions.ValidationException;

public class UserRoleException extends ValidationException {
    public UserRoleException(String message) {
        super(message);
    }

    public static UserRoleException ownerAlreadyExists() {
        return new UserRoleException("OWNER role already exists. Only one user can have the OWNER role at a time.");
    }

    public static UserRoleException cannotRemoveOwner() {
        return new UserRoleException("Cannot remove OWNER role. At least one user must have the OWNER role.");
    }

    public static UserRoleException invalidRole(String role) {
        return new UserRoleException("Invalid role: " + role + ". Valid roles are: USER, OWNER, ADMIN");
    }
}
