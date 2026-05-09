package com.upm.gym.controller;

import com.upm.gym.model.Membership;
import com.upm.gym.model.User;
import com.upm.gym.service.MembershipService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class MyMembershipController {

    @FXML private Label headerLabel;
    @FXML private GridPane detailsGrid;
    @FXML private Label planLabel;
    @FXML private Label statusLabel;
    @FXML private Label startDateLabel;
    @FXML private Label expiryDateLabel;
    @FXML private Label daysRemainingLabel;
    @FXML private Label messageLabel;
    @FXML private Button cancelButton;

    private final MembershipService membershipService = new MembershipService();
    private User loggedInUser;
    private Membership currentMembership;

    public void setUser(User user) {
        this.loggedInUser = user;
        loadMembership();
    }

    private void loadMembership() {
        try {
            currentMembership = membershipService.getActiveMembership(loggedInUser.getUserId());

            if (currentMembership == null) {
                showNoMembershipState();
                return;
            }

            populateFields(currentMembership);

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Failed to load membership details.");
            e.printStackTrace();
        }
    }

    private void populateFields(Membership m) {
        planLabel.setText(m.getMembershipType());
        startDateLabel.setText(m.getStartDate().toString());
        expiryDateLabel.setText(m.getExpiryDate().toString());

        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), m.getExpiryDate());

        if (daysRemaining < 0) {
            statusLabel.setText("Expired");
            statusLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
            daysRemainingLabel.setText("Expired " + Math.abs(daysRemaining) + " day(s) ago");
            cancelButton.setDisable(true);
        } else {
            statusLabel.setText("Active");
            statusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            daysRemainingLabel.setText(daysRemaining + " day(s)");
        }
    }

    private void showNoMembershipState() {
        headerLabel.setText("You don't have an active membership.");
        detailsGrid.setVisible(false);
        cancelButton.setVisible(false);

        messageLabel.setStyle("-fx-text-fill: #555;");
        messageLabel.setText("Go back to the dashboard and register for a plan.");
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        if (currentMembership == null) return;

        long daysSinceStart = ChronoUnit.DAYS.between(currentMembership.getStartDate(), LocalDate.now());
        boolean refundEligible = daysSinceStart <= 3;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Membership");
        confirm.setHeaderText("Are you sure you want to cancel?");
        confirm.setContentText(refundEligible
                ? "You are within the 3-day window. You are eligible for a refund."
                : "You are past the 3-day refund window. No refund will be issued.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        try {
            boolean success = membershipService.cancel(currentMembership.getMembershipId());
            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Membership cancelled successfully.");
                cancelButton.setDisable(true);
                statusLabel.setText("Cancelled");
                statusLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Cancellation failed. Please try again.");
            }
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("An error occurred. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MemberDashboard.fxml"));
            Parent root = loader.load();

            MemberDashboardController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Sce