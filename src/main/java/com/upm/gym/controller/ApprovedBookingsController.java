package com.upm.gym.controller;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApprovedBookingsController {

    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> idColumn;
    @FXML private TableColumn<Booking, String> requesterColumn;
    @FXML private TableColumn<Booking, String> facilityColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> timeColumn;
    @FXML private Label dateLabel;
    @FXML private Label messageLabel;

    private final BookingService bookingService = new BookingService();
    private User loggedInUser;
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configureColumns();
        bookingsTable.setItems(bookings);

        // Show today's date in the header
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        dateLabel.setText("Approved bookings for: " + today);
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        loadApprovedBookings();
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

    private void loadApprovedBookings() {
        try {
            List<Booking> result = bookingService.getApprovedBookingsForToday();
            bookings.setAll(result);
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Failed to load approved bookings.");
            e.printStackTrace();
        }
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