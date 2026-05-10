package com.upm.gym.controller;

import com.upm.gym.model.Membership;
import com.upm.gym.model.User;
import com.upm.gym.service.MembershipService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RegisterMembershipController {

    @FXML
    private ChoiceBox<String> planChoiceBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label messageLabel;

    private final MembershipService membershipService = new MembershipService();
    private User loggedInUser;

    /**
     * Called automatically by JavaFX after the FXML loads.
     * Use this to populate dropdowns and set defaults.
     */
    @FXML
    private void initialize() {
        planChoiceBox.setItems(FXCollections.observableArrayList(
                "1 Month", "1 Semester", "1 Year"
        ));

        startDatePicker.setValue(LocalDate.now());

        // Update the summary label when the user changes the plan or date
        planChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
    }

    public void setUser(User user) {
        this.loggedInUser = user;
    }

    private void updateSummary() {
        String plan = planChoiceBox.getValue();
        LocalDate startDate = startDatePicker.getValue();

        if (plan == null || startDate == null) {
            summaryLabel.setText("Select a plan to see the summary.");
            return;
        }

        double price = membershipService.getPriceForPlan(plan);
        LocalDate expiry = membershipService.calculateExpiryDate(plan, startDate);

        summaryLabel.setText(plan + " membership: SAR " + price +
                ", expires " + expiry + ".");
    }

    @FXML
    private void handleSubscribe(ActionEvent event) {
        String plan = planChoiceBox.getValue();
        LocalDate startDate = startDatePicker.getValue();

        if (plan == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please select a plan.");
            return;
        }
        if (startDate == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please select a start date.");
            return;
        }
        if (startDate.isBefore(LocalDate.now())) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Start date cannot be in the past.");
            return;
        }

        try {
            Membership membership = new Membership();
            membership.setUserId(loggedInUser.getUserId());
            membership.setMembershipType(plan);
            membership.setStartDate(startDate);
            membership.setExpiryDate(membershipService.calculateExpiryDate(plan, startDate));
            membership.setAutoRenew(false);

            double amount = membershipService.getPriceForPlan(plan);

            // Navigate to Payment screen with the prepared membership
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Payment.fxml"));
            Parent root = loader.load();

            PaymentController paymentController = loader.getController();
            paymentController.setUser(loggedInUser);
            paymentController.setMembershipDetails(membership, amount);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Payment");

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Could not proceed to payment. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        goBackToDashboard(event);
    }

    private void goBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MemberDashboard.fxml"));
            Parent root = loader.load();

            MemberDashboardController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Member Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}