package com.upm.gym.controller;

import com.upm.gym.model.User;
import com.upm.gym.service.BookingService;
import com.upm.gym.service.MembershipService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReportsController {

    @FXML private Label dateLabel;
    @FXML private Label totalMembersLabel;
    @FXML private Label activeMembershipsLabel;
    @FXML private Label bookingsTodayLabel;
    @FXML private Label pendingRequestsLabel;
    @FXML private HBox facilityCardsBox;

    private final MembershipService membershipService = new MembershipService();
    private final BookingService bookingService = new BookingService();
    private User loggedInUser;

    @FXML
    private void initialize() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        dateLabel.setText("Generated on " + today);
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        loadStats();
        loadChart();
    }

    private void loadStats() {
        try {
            totalMembersLabel.setText(String.valueOf(membershipService.countTotalMembers()));
            activeMembershipsLabel.setText(String.valueOf(membershipService.countActiveMemberships()));
            bookingsTodayLabel.setText(String.valueOf(bookingService.countBookingsToday()));
            pendingRequestsLabel.setText(String.valueOf(bookingService.countPendingBookings()));
        } catch (Exception e) {
            totalMembersLabel.setText("?");
            activeMembershipsLabel.setText("?");
            bookingsTodayLabel.setText("?");
            pendingRequestsLabel.setText("?");
            e.printStackTrace();
        }
    }

    private void loadChart() {
        try {
            Map<String, Integer> data = bookingService.countBookingsByFacilityThisMonth();

            String[] colors = {"#16a085", "#d35400", "#8e44ad", "#2980b9"};
            int colorIndex = 0;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                VBox card = new VBox(8);
                card.setAlignment(javafx.geometry.Pos.CENTER);
                card.setPrefHeight(100);
                card.setPrefWidth(180);
                card.setStyle(
                        "-fx-background-color: " + colors[colorIndex % colors.length] + ";" +
                                " -fx-background-radius: 8;" +
                                " -fx-padding: 15;");

                Label nameLabel = new Label(entry.getKey().toUpperCase());
                nameLabel.setStyle(
                        "-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: white;");

                Label countLabel = new Label(String.valueOf(entry.getValue()));
                countLabel.setStyle(
                        "-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

                card.getChildren().addAll(nameLabel, countLabel);
                facilityCardsBox.getChildren().add(card);

                colorIndex++;
            }

        } catch (Exception e) {
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