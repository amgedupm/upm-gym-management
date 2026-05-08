package com.upm.gym.dao;

import com.upm.gym.enums.BookingStatus;
import com.upm.gym.exception.DatabaseException;
import com.upm.gym.model.Booking;
import com.upm.gym.util.DBConnection;

import java.sql.*;

public class BookingDAO {

    public void createBooking(
            Booking booking) {

        String sql =
                "INSERT INTO bookings " +
                "(user_id, facility_name, " +
                "booking_date, start_time, " +
                "end_time, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn =
                     DBConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1,
                    booking.getUserId());

            stmt.setString(2,
                    booking.getFacilityName());

            stmt.setDate(3,
                    Date.valueOf(
                            booking.getBookingDate()));

            stmt.setTime(4,
                    Time.valueOf(
                            booking.getStartTime()));

            stmt.setTime(5,
                    Time.valueOf(
                            booking.getEndTime()));

            stmt.setString(6,
                    booking.getStatus().name());

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to create booking."
            );
        }
    }

    public void updateBookingStatus(
            int bookingId,
            BookingStatus status) {

        String sql =
                "UPDATE bookings " +
                "SET status=? WHERE booking_id=?";

        try (Connection conn =
                     DBConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, bookingId);

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to update booking."
            );
        }
    }
}