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
    private void handleRegisterMembership(ActionEvent event) {
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

    @FXML
    private void handleMyMembership(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MyMembership.fxml"));
            Parent root = loader.load();

            MyMembershipController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - My Membership");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBookField(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewBooking.fxml"));
            Parent root = loader.load();

            NewBookingController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Book a Field");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMyBookings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MyBookings.fxml"));
            Parent root = loader.load();

            MyBookingsController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - My Bookings");
        } catch (Exception e) {
            e.printStackTrace();
        }
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