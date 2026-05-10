package com.upm.gym.controller;

import com.upm.gym.model.Membership;
import com.upm.gym.model.User;
import com.upm.gym.service.MembershipService;
import com.upm.gym.service.UserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MemberLookupController {

    @FXML private TextField userIdField;
    @FXML private Label messageLabel;
    @FXML private VBox resultBox;
    @FXML private Label fullNameLabel;
    @FXML private Label userIdLabel;
    @FXML private Label roleLabel;
    @FXML private Label membershipLabel;
    @FXML private Label membershipStatusLabel;

    private final UserService userService = new UserService();
    private final MembershipService membershipService = new MembershipService();
    private User loggedInUser;

    public void setUser(User user) {
        this.loggedInUser = user;
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = userIdField.getText().trim();

        if (query.isEmpty()) {
            showError("Please enter a user ID to search.");
            return;
        }

        try {
            User foundUser = userService.findById(query);

            if (foundUser == null) {
                resultBox.setVisible(false);
                showError("No member found with ID '" + query + "'.");
                return;
            }

            populateUserDetails(foundUser);
            populateMembershipDetails(foundUser);
            resultBox.setVisible(true);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Member found.");

        } catch (Exception e) {
            showError("An error occurred during search. Please try again.");
            e.printStackTrace();
        }
    }

    private void populateUserDetails(User user) {
        fullNameLabel.setText(user.getFullName());
        userIdLabel.setText(user.getUserId());
        roleLabel.setText(user.getRole().name());
    }

    private void populateMembershipDetails(User user) {
        Membership membership = membershipService.getActiveMembership(user.getUserId());

        if (membership == null) {
            membershipLabel.setText("None");
            membershipLabel.setStyle("-fx-text-fill: #555;");
            membershipStatusLabel.setText("No active membership");
            membershipStatusLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
            return;
        }

        membershipLabel.setText(membership.getMembershipType() +
                " (until " + membership.getExpiryDate() + ")");
        membershipLabel.setStyle("-fx-text-fill: black;");

        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), membership.getExpiryDate());

        if (daysRemaining < 0) {
            membershipStatusLabel.setText("Expired");
            membershipStatusLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
        } else {
            membershipStatusLabel.setText("Active (" + daysRemaining + " days remaining)");
            membershipStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        }
    }

    private void showError(String text) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(text);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CoachDashboard.fxml"));
            Parent root = loader.load();

            CoachDashboardController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Coach Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}