package com.upm.gym.service;

import com.upm.gym.dao.UserDAO;
import com.upm.gym.model.User;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
    }

    public User login(
            String userId,
            String password) {

        if (userId == null ||
            password == null) {

            return null;
        }

        return userDAO.login(
                userId,
                password
        );
    }
}
