package com.upm.gym.controller;

import com.upm.gym.enums.BookingStatus;
import com.upm.gym.exception.BookingException;
import com.upm.gym.model.Booking;
import com.upm.gym.model.User;
import com.upm.gym.service.BookingService;

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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class NewBookingController {

    @FXML private ChoiceBox<String> facilityChoice;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> startMinuteSpinner;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;
    @FXML private Label summaryLabel;
    @FXML private Label messageLabel;

    private final BookingService bookingService = new BookingService();
    private User loggedInUser;

    // Gym operating hours
    private static final int OPEN_HOUR = 6;   // 6 AM
    private static final int CLOSE_HOUR = 23; // 11 PM
    private static final int MAX_DURATION_HOURS = 2;

    @FXML
    private void initialize() {
        facilityChoice.setItems(FXCollections.observableArrayList(
                "Football Field", "Basketball Court"
        ));

        datePicker.setValue(LocalDate.now().plusDays(1));

        startHourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(OPEN_HOUR, CLOSE_HOUR - 1, 18));
        startMinuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15));

        endHourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(OPEN_HOUR + 1, CLOSE_HOUR, 19));
        endMinuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15));

        // Update summary live
        facilityChoice.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        startHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        startMinuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        endHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
        endMinuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSummary());
    }

    public void setUser(User user) {
        this.loggedInUser = user;
    }

    private void updateSummary() {
        String facility = facilityChoice.getValue();
        LocalDate date = datePicker.getValue();

        if (facility == null || date == null) {
            summaryLabel.setText("Fill in the form to see a summary.");
            return;
        }

        LocalTime start = readStartTime();
        LocalTime end = readEndTime();

        summaryLabel.setText(facility + " on " + date + " from " + start + " to " + end + ".");
    }

    private LocalTime readStartTime() {
        return LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
    }

    private LocalTime readEndTime() {
        return LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        String facility = facilityChoice.getValue();
        LocalDate date = datePicker.getValue();

        // Required fields
        if (facility == null) {
            showError("Please select a facility.");
            return;
        }
        if (date == null) {
            showError("Please select a date.");
            return;
        }

        // Date must be today or in the future
        if (date.isBefore(LocalDate.now())) {
            showError("Booking date cannot be in the past.");
            return;
        }

        LocalTime start = readStartTime();
        LocalTime end = readEndTime();

        // End after start
        if (!end.isAfter(start)) {
            showError("End time must be after start time.");
            return;
        }

        // Duration check
        long minutes = ChronoUnit.MINUTES.between(start, end);
        if (minutes > MAX_DURATION_HOURS * 60) {
            showError("Bookings cannot exceed " + MAX_DURATION_HOURS + " hours.");
            return;
        }

        // Within gym hours
        if (start.getHour() < OPEN_HOUR || end.getHour() > CLOSE_HOUR ||
                (end.getHour() == CLOSE_HOUR && end.getMinute() > 0)) {
            showError("Booking must be between " + OPEN_HOUR + ":00 and " + CLOSE_HOUR + ":00.");
            return;
        }

        // If date is today, start time must still be in the future
        if (date.equals(LocalDate.now()) && !start.isAfter(LocalTime.now())) {
            showError("Start time must be in the future.");
            return;
        }

        // All good — submit
        try {
            Booking booking = new Booking();
            booking.setUserId(loggedInUser.getUserId());
            booking.setFacilityName(facility);
            booking.setBookingDate(date);
            booking.setStartTime(start);
            booking.setEndTime(end);
            booking.setStatus(BookingStatus.PENDING);

            bookingService.submitBooking(booking);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Booking request submitted! Pending coach approval.");

            // Return to dashboard after 1.5 seconds
            javafx.animation.PauseTransition pause =
                    new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            pause.setOnFinished(e -> goBackToDashboard(event));
            pause.play();

        } catch (BookingException be) {
            showError(be.getMessage());
        } catch (Exception e) {
            showError("Submission failed. Please try again.");
            e.printStackTrace();
        }
    }

    private void showError(String text) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(text);
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