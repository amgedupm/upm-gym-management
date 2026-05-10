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

public class CoachDashboardController {

    @FXML
    private Label welcomeLabel;

    private User loggedInUser;

    public void setUser(User user) {
        this.loggedInUser = user;
        welcomeLabel.setText("Welcome, " + user.getFullName());
    }

    @FXML
    private void handlePendingBookings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PendingBookings.fxml"));
            Parent root = loader.load();

            PendingBookingsController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Pending Bookings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleApprovedBookings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ApprovedBookings.fxml"));
            Parent root = loader.load();

            ApprovedBookingsController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Approved Bookings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMemberLookup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MemberLookup.fxml"));
            Parent root = loader.load();

            MemberLookupController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Member Lookup");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReports(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Reports.fxml"));
            Parent root = loader.load();

            ReportsController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Reports");
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