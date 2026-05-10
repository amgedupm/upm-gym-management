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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PaymentController {

    @FXML private Label planLabel;
    @FXML private Label amountLabel;
    @FXML private TextField holderField;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private Label messageLabel;

    private final MembershipService membershipService = new MembershipService();
    private User loggedInUser;
    private Membership pendingMembership;
    private double amount;

    public void setUser(User user) {
        this.loggedInUser = user;
    }

    /**
     * Called by RegisterMembershipController to pass the prepared
     * membership and the amount to charge.
     */
    public void setMembershipDetails(Membership membership, double amount) {
        this.pendingMembership = membership;
        this.amount = amount;
        planLabel.setText(membership.getMembershipType());
        amountLabel.setText("SAR " + amount);
    }

    @FXML
    private void handlePay(ActionEvent event) {
        String holder = holderField.getText().trim();
        String cardNumber = cardNumberField.getText().trim().replaceAll("\\s", "");
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();

        // Validation
        if (holder.isEmpty()) {
            showError("Please enter the card holder name.");
            return;
        }
        if (cardNumber.isEmpty()) {
            showError("Please enter your card number.");
            return;
        }
        if (!cardNumber.matches("\\d{16}")) {
            showError("Card number must be exactly 16 digits.");
            return;
        }
        if (!expiry.matches("\\d{2}/\\d{2}")) {
            showError("Expiry must be in MM/YY format.");
            return;
        }
        int month = Integer.parseInt(expiry.substring(0, 2));
        if (month < 1 || month > 12) {
            showError("Expiry month must be between 01 and 12.");
            return;
        }
        if (!cvv.matches("\\d{3}")) {
            showError("CVV must be exactly 3 digits.");
            return;
        }

        try {
            String last4 = cardNumber.substring(12);
            boolean paid = membershipService.processPayment(
                    loggedInUser.getUserId(), amount, last4);

            if (!paid) {
                showError("Payment was declined. Please try again.");
                return;
            }

            // Payment OK — register the membership now
            membershipService.register(pendingMembership);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Payment successful! Generating receipt...");

            // Move to receipt after a short delay
            javafx.animation.PauseTransition pause =
                    new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.2));
            pause.setOnFinished(e -> openReceipt(event, last4));
            pause.play();

        } catch (Exception e) {
            showError("Payment failed. Please try again.");
            e.printStackTrace();
        }
    }

    private void openReceipt(ActionEvent event, String last4) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Receipt.fxml"));
            Parent root = loader.load();

            ReceiptController controller = loader.getController();
            controller.setUser(loggedInUser);
            controller.setReceiptData(pendingMembership, amount, last4);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Receipt");

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to dashboard if receipt fails to load
            goBackToDashboard(event);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterMembership.fxml"));
            Parent root = loader.load();

            RegisterMembershipController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Register Membership");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String text) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(text);
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