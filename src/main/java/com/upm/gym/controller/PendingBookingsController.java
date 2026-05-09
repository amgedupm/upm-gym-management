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

import java.util.List;
import java.util.Optional;

public class PendingBookingsController {

    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> idColumn;
    @FXML private TableColumn<Booking, String> requesterColumn;
    @FXML private TableColumn<Booking, String> facilityColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> timeColumn;
    @FXML private Label messageLabel;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;

    private final BookingService bookingService = new BookingService();
    private User loggedInUser;
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureColumns();
        bookingsTable.setItems(bookings);

        // Enable buttons whenever any row is selected
        bookingsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    boolean noSelection = (newVal == null);
                    approveButton.setDisable(noSelection);
                    rejectButton.setDisable(noSelection);
                }
        );
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        loadPendingBookings();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getBookingId())));

        requesterColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getUserId()));

        facilityColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getFacilityName()));

        dateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getBookingDate().toString()));

        timeColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getStartTime() + " – " + cell.getValue().getEndTime()));
    }

    private void loadPendingBookings() {
        try {
            List<Booking> result = bookingService.getPendingBookings();
            bookings.setAll(result);
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Failed to load pending bookings.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleApprove(ActionEvent event) {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (!confirm("Approve booking #" + selected.getBookingId() + "?",
                "This will notify the requester that their booking is approved.")) return;

        try {
            bookingService.approveBooking(selected.getBookingId());
            bookings.remove(selected);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Booking #" + selected.getBookingId() + " approved.");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Approval failed. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReject(ActionEvent event) {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (!confirm("Reject booking #" + selected.getBookingId() + "?",
                "This will notify the requester that their booking is rejected.")) return;

        try {
            bookingService.rejectBooking(selected.getBookingId());
            bookings.remove(selected);
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Booking #" + selected.getBookingId() + " rejected.");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Rejection failed. Please try again.");
            e.printStackTrace();
        }
    }

    private boolean confirm(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
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