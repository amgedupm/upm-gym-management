package com.upm.gym.controller;

import com.upm.gym.enums.BookingStatus;
import com.upm.gym.model.Booking;
import com.upm.gym.model.User;
import com.upm.gym.service.BookingService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MyBookingsController {

    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> idColumn;
    @FXML private TableColumn<Booking, String> facilityColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> timeColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private Label messageLabel;
    @FXML private Button cancelButton;

    private final BookingService bookingService = new BookingService();
    private User loggedInUser;
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureColumns();

        bookingsTable.setItems(bookings);

        // Enable cancel button only when a row is selected and the booking is cancellable
        bookingsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateCancelButtonState(newVal)
        );
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        loadBookings();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getBookingId())));

        facilityColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getFacilityName()));

        dateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getBookingDate().toString()));

        timeColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getStartTime() + " – " + cell.getValue().getEndTime()));

        statusColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStatus().name()));
    }

    private void loadBookings() {
        try {
            List<Booking> result = bookingService.getBookingsByUser(loggedInUser.getUserId());
            bookings.setAll(result);
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Failed to load bookings.");
            e.printStackTrace();
        }
    }

    private void updateCancelButtonState(Booking selected) {
        if (selected == null) {
            cancelButton.setDisable(true);
            return;
        }

        boolean isApproved = selected.getStatus() == BookingStatus.APPROVED;

        // FR-11: cancellation only allowed up to 12 hours before start time
        LocalDateTime bookingStart = selected.getBookingDate().atTime(selected.getStartTime());
        long hoursUntilStart = Duration.between(LocalDateTime.now(), bookingStart).toHours();
        boolean isWithinWindow = hoursUntilStart >= 12;

        cancelButton.setDisable(!(isApproved && isWithinWindow));
    }

    @FXML
    private void handleCancelBooking(ActionEvent event) {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Booking");
        confirm.setHeaderText("Cancel booking #" + selected.getBookingId() + "?");
        confirm.setContentText(
                selected.getFacilityName() + " on " +
                        selected.getBookingDate() + " at " +
                        selected.getStartTime() + ".");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        try {
            boolean success = bookingService.cancelBooking(selected.getBookingId());
            if (success) {
                selected.setStatus(BookingStatus.CANCELLED);
                bookingsTable.refresh();
                cancelButton.setDisable(true);

                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Booking #" + selected.getBookingId() + " cancelled.");
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
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Member Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}