package com.upm.gym.controller;

import com.upm.gym.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MemberDashboardController {

    @FXML
    private Label welcomeLabel;

    private User loggedInUser;

    public void setUser(User user) {
        this.loggedInUser = user;
        welcomeLabel.setText("Welcome, " + user.getFullName());
    }

    @FXML
    private void handleRegisterMembership() {
        System.out.println("TODO: open Register Membership screen");
    }

    @FXML
    private void handleMyMembership() {
        System.out.println("TODO: open My Membership screen");
    }

    @FXML
    private void handleBookField() {
        System.out.println("TODO: open Book Field screen");
    }

    @FXML
    private void handleMyBookings() {
        System.out.println("TODO: open My Bookings screen");
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("UPM Gym Management System");
    }
}