package com.upm.gym.service;

import com.upm.gym.dao.UserDAO;
import com.upm.gym.model.User;

/**
 * Service layer for user-related operations.
 * Uses UserDAO for database access.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    /**
     * Retrieves a user from the database by ID.
     */
    public User findById(String userId) {

        if (userId == null ||
                userId.isBlank()) {

            return null;
        }

        return userDAO.findById(
                userId.trim()
        );
    }
}