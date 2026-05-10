package com.upm.gym.controller;

import com.upm.gym.model.User;
import com.upm.gym.service.BookingService;
import com.upm.gym.service.MembershipService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
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
    @FXML private BarChart<String, Number> facilityChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

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
            // If anything fails, show "?" instead of crashing
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

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Bookings");

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            facilityChart.setData(FXCollections.observableArrayList(series));
            facilityChart.setAnimated(false);

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