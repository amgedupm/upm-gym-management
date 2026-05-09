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
    private void handleLogin(javafx.event.ActionEvent event) {
        String userId = userIdField.getText().trim();
        String password = passwordField.getText();

        if (userId.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter both User ID and password.");
            return;
        }

        try {
            User user = authService.login(userId, password);

            if (user == null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Invalid credentials. Please try again.");
                passwordField.clear();
                return;
            }

            // Login succeeded — open the appropriate dashboard
            openMemberDashboard(event, user);

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Database error. Please try again later.");
            e.printStackTrace();
        }
    }

    private void openMemberDashboard(javafx.event.ActionEvent event, User user) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fxml/MemberDashboard.fxml"));
        javafx.scene.Parent root = loader.load();

        MemberDashboardController dashboardController = loader.getController();
        dashboardController.setUser(user);

        javafx.stage.Stage stage = (javafx.stage.Stage)
                ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(root));
        stage.setTitle("UPM Gym - Member Dashboard");
    }
}