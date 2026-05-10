package com.upm.gym.service;

import com.upm.gym.enums.Role;
import com.upm.gym.model.User;

/**
 * STUB IMPLEMENTATION — frontend placeholder.
 * The real implementation will be added by the backend lead.
 * Once the real version exists, replace the stub bodies with calls to UserDAO.
 */
public class UserService {

    /**
     * STUB — returns a hardcoded user if the ID matches one of a few sample IDs.
     * Real version will query UserDAO.findById(userId).
     */
    public User findById(String userId) {
        if (userId == null || userId.isBlank()) {
            return null;
        }

        // Sample matches for testing
        return switch (userId.trim()) {
            case "4410097" -> new User(
                    "4410097", "Ahmed Hakimi", "1234", Role.STUDENT);
            case "4413828" -> new User(
                    "4413828", "Sara Al-Otaibi", "1234", Role.STUDENT);
            case "4510353" -> new User(
                    "4510353", "Khaled Al-Ghamdi", "1234", Role.FACULTY);
            case "coach01" -> new User(
                    "coach01", "Coach Ali", "coach123", Role.COACH);
            default -> null;
        };
    }
}