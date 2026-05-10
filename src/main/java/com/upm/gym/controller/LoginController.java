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
            routeToDashboard(event, user);

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Database error. Please try again later.");
            e.printStackTrace();
        }
    }

    private void routeToDashboard(javafx.event.ActionEvent event, User user) throws Exception {
        String fxmlPath;
        String windowTitle;

        switch (user.getRole()) {
            case STUDENT, FACULTY -> {
                fxmlPath = "/fxml/MemberDashboard.fxml";
                windowTitle = "UPM Gym - Member Dashboard";
            }
            case COACH, STAFF -> {
                fxmlPath = "/fxml/CoachDashboard.fxml";
                windowTitle = "UPM Gym - Coach Dashboard";
            }
            case SECURITY -> {
                fxmlPath = "/fxml/SecuritySearch.fxml";
                windowTitle = "UPM Gym - Security Verification";
            }
            default -> {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Unknown role. Contact admin.");
                return;
            }
        }

        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxmlPath));
        javafx.scene.Parent root = loader.load();

        // Pass the user object to whichever controller was loaded
        Object controller = loader.getController();
        if (controller instanceof MemberDashboardController memberController) {
            memberController.setUser(user);
        } else if (controller instanceof CoachDashboardController coachController) {
            coachController.setUser(user);
        } else if (controller instanceof SecuritySearchController securityController) {
            securityController.setUser(user);
        }

        javafx.stage.Stage stage = (javafx.stage.Stage)
                ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(root));
        stage.setTitle(windowTitle);
    }
}