package com.upm.gym.controller;

import com.upm.gym.enums.Role;
import com.upm.gym.model.User;
import com.upm.gym.service.AuthService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField userIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String userId = userIdField.getText().trim();
        String password = passwordField.getText();

        if (userId.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both User ID and password.");
            return;
        }

        User user = authService.login(userId, password);

        if (user == null) {
            messageLabel.setText("Invalid credentials. Please try again.");
            passwordField.clear();
            return;
        }

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("Welcome, " + user.getFullName() + "! Role: " + user.getRole());

        // TODO: navigate to the correct dashboard based on role
        // We'll add this once the dashboards exist.
    }
}