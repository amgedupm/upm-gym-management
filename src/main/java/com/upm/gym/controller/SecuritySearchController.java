package com.upm.gym.controller;

import com.upm.gym.model.Booking;
import com.upm.gym.model.User;
import com.upm.gym.service.BookingService;
import com.upm.gym.service.UserService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class SecuritySearchController {

    @FXML private Label welcomeLabel;
    @FXML private TextField userIdField;
    @FXML private Label statusLabel;
    @FXML private Label userInfoLabel;
    @FXML private Label bookingsHeaderLabel;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> idColumn;
    @FXML private TableColumn<Booking, String> facilityColumn;
    @FXML private TableColumn<Booking, String> timeColumn;

    private final UserService userService = new UserService();
    private final BookingService bookingService = new BookingService();
    private User loggedInUser;
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureColumns();
        bookingsTable.setItems(bookings);
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        welcomeLabel.setText("Welcome, " + user.getFullName());
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getBookingId())));

        facilityColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getFacilityName()));

        timeColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getStartTime() + " – " + cell.getValue().getEndTime()));
    }

    @FXML
    private void handleVerify(ActionEvent event) {
        String query = userIdField.getText().trim();

        if (query.isEmpty()) {
            showStatus("ENTER A USER ID", false, false);
            userInfoLabel.setText("");
            hideBookings();
            return;
        }

        try {
            User foundUser = userService.findById(query);

            if (foundUser == null) {
                showStatus("USER NOT FOUND", false, true);
                userInfoLabel.setText("No user matches ID '" + query + "'.");
                hideBookings();
                return;
            }

            List<Booking> approvedToday = bookingService.getApprovedBookingsForUserToday(query);

            userInfoLabel.setText(
                    foundUser.getFullName() + " — " + foundUser.getRole().name());

            if (approvedToday.isEmpty()) {
                showStatus("NOT VALID — NO BOOKING TODAY", false, true);
                hideBookings();
            } else {
                showStatus("VALID ENTRY", true, true);
                bookings.setAll(approvedToday);
                bookingsTable.setVisible(true);
                bookingsHeaderLabel.setVisible(true);
            }

        } catch (Exception e) {
            showStatus("ERROR — TRY AGAIN", false, true);
            userInfoLabel.setText("");
            hideBookings();
            e.printStackTrace();
        }
    }

    private void showStatus(String text, boolean success, boolean hasResult) {
        statusLabel.setText("  " + text + "  ");
        if (!hasResult) {
            statusLabel.setStyle(
                    "-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 5;" +
                            " -fx-background-color: #f5f7fa; -fx-text-fill: #555;");
        } else if (success) {
            statusLabel.setStyle(
                    "-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 5;" +
                            " -fx-background-color: #27ae60; -fx-text-fill: white;");
        } else {
            statusLabel.setStyle(
                    "-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 5;" +
                            " -fx-background-color: #c0392b; -fx-text-fill: white;");
        }
    }

    private void hideBookings() {
        bookings.clear();
        bookingsTable.setVisible(false);
        bookingsHeaderLabel.setVisible(false);
    }

    @FXML
    private void handleClear(ActionEvent event) {
        userIdField.clear();
        statusLabel.setText("");
        statusLabel.setStyle("");
        userInfoLabel.setText("");
        hideBookings();
        userIdField.requestFocus();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym Management System");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}