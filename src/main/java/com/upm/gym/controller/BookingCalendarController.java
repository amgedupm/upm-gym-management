package com.upm.gym.controller;

import com.upm.gym.enums.BookingStatus;
import com.upm.gym.model.Booking;
import com.upm.gym.model.User;
import com.upm.gym.service.BookingService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class BookingCalendarController {

    @FXML private ChoiceBox<String> facilityChoice;
    @FXML private Label weekRangeLabel;
    @FXML private GridPane calendarGrid;

    private final BookingService bookingService = new BookingService();
    private User loggedInUser;

    // Gym hours (matches NewBooking and your earlier preference)
    private static final int OPENING_HOUR = 9;   // 9 AM
    private static final int CLOSING_HOUR = 19;  // 7 PM (last slot starts at 18:00, ends 19:00)

    private LocalDate weekStart;  // Monday of currently displayed week

    @FXML
    private void initialize() {
        facilityChoice.setItems(FXCollections.observableArrayList(
                "Football Field", "Basketball Court"
        ));
        facilityChoice.setValue("Football Field");

        // Re-render the grid whenever the user changes facility
        facilityChoice.valueProperty().addListener((obs, o, n) -> renderCalendar());

        weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public void setUser(User user) {
        this.loggedInUser = user;
        renderCalendar();
    }

    private void renderCalendar() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        String facility = facilityChoice.getValue();
        if (facility == null) return;

        LocalDate weekEnd = weekStart.plusDays(6);
        weekRangeLabel.setText(formatRange(weekStart, weekEnd));

        // Fetch all bookings for this facility this week
        List<Booking> bookings = bookingService.getBookingsForFacilityInRange(
                facility, weekStart, weekEnd);

        // Header row: corner + Mon-Sun column headers
        calendarGrid.add(headerCell(""), 0, 0);
        for (int day = 0; day < 7; day++) {
            LocalDate date = weekStart.plusDays(day);
            String header = date.getDayOfWeek().toString().substring(0, 3) +
                    "\n" + date.getMonthValue() + "/" + date.getDayOfMonth();
            calendarGrid.add(headerCell(header), day + 1, 0);
        }

        // Time rows
        int row = 1;
        for (int hour = OPENING_HOUR; hour < CLOSING_HOUR; hour++) {
            // Time label cell on the left
            String timeLabel = String.format("%02d:00", hour);
            calendarGrid.add(timeCell(timeLabel), 0, row);

            // 7 day cells for this hour
            for (int day = 0; day < 7; day++) {
                LocalDate date = weekStart.plusDays(day);
                LocalTime time = LocalTime.of(hour, 0);

                Booking found = findBookingFor(bookings, date, time);
                Button cell = buildCellButton(date, time, found);
                calendarGrid.add(cell, day + 1, row);
            }
            row++;
        }
    }

    private Booking findBookingFor(List<Booking> bookings, LocalDate date, LocalTime time) {
        for (Booking b : bookings) {
            if (!b.getBookingDate().equals(date)) continue;
            // Slot is "covered" if the booking starts at or before this time
            // and ends at or after the next hour
            LocalTime nextHour = time.plusHours(1);
            if (!b.getStartTime().isAfter(time)
                    && !b.getEndTime().isBefore(nextHour)) {
                return b;
            }
        }
        return null;
    }

    private Button buildCellButton(LocalDate date, LocalTime time, Booking booking) {
        Button cell = new Button();
        cell.setPrefSize(110, 50);
        cell.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(cell, Priority.ALWAYS);

        LocalDateTime slotStart = LocalDateTime.of(date, time);
        boolean isPast = slotStart.isBefore(LocalDateTime.now());

        if (isPast) {
            cell.setText("");
            cell.setStyle(
                    "-fx-background-color: #bdc3c7; -fx-background-radius: 4;");
            cell.setDisable(true);
            return cell;
        }

        if (booking != null) {
            BookingStatus status = booking.getStatus();
            if (status == BookingStatus.APPROVED) {
                cell.setText("Booked");
                cell.setStyle(
                        "-fx-background-color: #c0392b; -fx-background-radius: 4;" +
                                " -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else if (status == BookingStatus.PENDING) {
                cell.setText("Pending");
                cell.setStyle(
                        "-fx-background-color: #f39c12; -fx-background-radius: 4;" +
                                " -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else {
                // CANCELLED or REJECTED — show as free
                cell.setText("Free");
                cell.setStyle(
                        "-fx-background-color: #27ae60; -fx-background-radius: 4;" +
                                " -fx-text-fill: white; -fx-font-size: 11px;");
                cell.setOnAction(e -> openBookingForm(e, date, time));
            }
            cell.setDisable(status == BookingStatus.APPROVED || status == BookingStatus.PENDING);
            return cell;
        }

        // Free
        cell.setText("Free");
        cell.setStyle(
                "-fx-background-color: #27ae60; -fx-background-radius: 4;" +
                        " -fx-text-fill: white; -fx-font-size: 11px;");
        cell.setOnAction(e -> openBookingForm(e, date, time));
        return cell;
    }

    private Label headerCell(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(110, 45);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle(
                "-fx-background-color: #34495e; -fx-text-fill: white;" +
                        " -fx-font-weight: bold; -fx-font-size: 12px;" +
                        " -fx-background-radius: 4; -fx-padding: 6;");
        label.setWrapText(true);
        GridPane.setHgrow(label, Priority.ALWAYS);
        return label;
    }

    private Label timeCell(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(80, 50);
        label.setStyle(
                "-fx-background-color: #ecf0f1; -fx-font-weight: bold;" +
                        " -fx-font-size: 12px; -fx-background-radius: 4;");
        return label;
    }

    private String formatRange(LocalDate start, LocalDate end) {
        DateTimeFormatter dayMonth = DateTimeFormatter.ofPattern("MMM d");
        DateTimeFormatter dayMonthYear = DateTimeFormatter.ofPattern("MMM d, yyyy");
        return start.format(dayMonth) + " – " + end.format(dayMonthYear);
    }

    @FXML
    private void handlePrevWeek(ActionEvent event) {
        weekStart = weekStart.minusWeeks(1);
        renderCalendar();
    }

    @FXML
    private void handleNextWeek(ActionEvent event) {
        weekStart = weekStart.plusWeeks(1);
        renderCalendar();
    }

    @FXML
    private void handleToday(ActionEvent event) {
        weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        renderCalendar();
    }

    private void openBookingForm(ActionEvent event, LocalDate date, LocalTime time) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewBooking.fxml"));
            Parent root = loader.load();

            NewBookingController controller = loader.getController();
            controller.setUser(loggedInUser);
            controller.prefillFromCalendar(facilityChoice.getValue(), date, time);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Book a Field");
        } catch (Exception e) {
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