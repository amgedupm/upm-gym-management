package com.upm.gym.dao;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;


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
    public List<Booking> getBookingsByUser(
        String userId) {

    List<Booking> bookings =
            new ArrayList<>();

    String sql =
            "SELECT * FROM bookings " +
            "WHERE user_id = ? " +
            "ORDER BY booking_date DESC";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setString(1, userId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            bookings.add(
                    new Booking(
                            rs.getInt("booking_id"),
                            rs.getString("user_id"),
                            rs.getString("facility_name"),
                            rs.getDate("booking_date")
                                    .toLocalDate(),
                            rs.getTime("start_time")
                                    .toLocalTime(),
                            rs.getTime("end_time")
                                    .toLocalTime(),
                            BookingStatus.valueOf(
                                    rs.getString("status"))
                    )
            );
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to fetch bookings."
        );
    }

    return bookings;
    }
    public boolean cancelBooking(
        int bookingId) {

    String sql =
            "UPDATE bookings " +
            "SET status = 'CANCELLED' " +
            "WHERE booking_id = ?";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setInt(1, bookingId);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to cancel booking."
        );
    }
    }
    public List<Booking> getPendingBookings() {

    List<Booking> bookings =
            new ArrayList<>();

    String sql =
            "SELECT * FROM bookings " +
            "WHERE status = 'PENDING' " +
            "ORDER BY booking_date";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            bookings.add(
                    new Booking(
                            rs.getInt("booking_id"),
                            rs.getString("user_id"),
                            rs.getString("facility_name"),
                            rs.getDate("booking_date")
                                    .toLocalDate(),
                            rs.getTime("start_time")
                                    .toLocalTime(),
                            rs.getTime("end_time")
                                    .toLocalTime(),
                            BookingStatus.valueOf(
                                    rs.getString("status"))
                    )
            );
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to fetch pending bookings."
        );
    }

    return bookings;
    }
    public List<Booking> getApprovedBookingsByDate(
        LocalDate date) {

    List<Booking> bookings =
            new ArrayList<>();

    String sql =
            "SELECT * FROM bookings " +
            "WHERE status = 'APPROVED' " +
            "AND booking_date = ?";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setDate(1, Date.valueOf(date));

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            bookings.add(
                    new Booking(
                            rs.getInt("booking_id"),
                            rs.getString("user_id"),
                            rs.getString("facility_name"),
                            rs.getDate("booking_date")
                                    .toLocalDate(),
                            rs.getTime("start_time")
                                    .toLocalTime(),
                            rs.getTime("end_time")
                                    .toLocalTime(),
                            BookingStatus.valueOf(
                                    rs.getString("status"))
                    )
            );
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to fetch approved bookings."
        );
    }

    return bookings;
    }
    public List<Booking>
getApprovedBookingsByUserAndDate(
        String userId,
        LocalDate date) {

    List<Booking> bookings =
            new ArrayList<>();

    String sql =
            "SELECT * FROM bookings " +
            "WHERE user_id = ? " +
            "AND status = 'APPROVED' " +
            "AND booking_date = ?";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setString(1, userId);

        stmt.setDate(
                2,
                Date.valueOf(date)
        );

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            bookings.add(
                    mapBooking(rs)
            );
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to retrieve user bookings."
        );
    }

    return bookings;
    }
    public int countBookingsToday() {

    String sql =
            "SELECT COUNT(*) " +
            "FROM bookings " +
            "WHERE booking_date = CURDATE()";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql);

         ResultSet rs =
                 stmt.executeQuery()) {

        if (rs.next()) {

            return rs.getInt(1);
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to count bookings."
        );
    }

    return 0;
    }
    public int countPendingBookings() {

    String sql =
            "SELECT COUNT(*) " +
            "FROM bookings " +
            "WHERE status = 'PENDING'";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql);

         ResultSet rs =
                 stmt.executeQuery()) {

        if (rs.next()) {

            return rs.getInt(1);
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to count pending bookings."
        );
    }

    return 0;
    }
    public Map<String, Integer>
countBookingsByFacilityThisMonth() {

    Map<String, Integer> map =
            new LinkedHashMap<>();

    String sql =
            "SELECT facility_name, COUNT(*) " +
            "FROM bookings " +
            "WHERE MONTH(booking_date) = MONTH(CURDATE()) " +
            "GROUP BY facility_name";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql);

         ResultSet rs =
                 stmt.executeQuery()) {

        while (rs.next()) {

            map.put(
                    rs.getString(1),
                    rs.getInt(2)
            );
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to count bookings."
        );
    }

    return map;
    }
    private Booking mapBooking(ResultSet rs) {
    try {
        Booking booking = new Booking();

        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getString("user_id"));
        booking.setFacilityName(rs.getString("facility_name"));
        booking.setBookingDate(rs.getDate("booking_date").toLocalDate());
        booking.setStartTime(rs.getTime("start_time").toLocalTime());
        booking.setEndTime(rs.getTime("end_time").toLocalTime());
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));

        return booking;

    } catch (SQLException e) {
        throw new DatabaseException("Failed to map Booking from ResultSet", e);
    }
}
    public List<Booking>
getBookingsForFacilityInRange(
        String facility,
        LocalDate from,
        LocalDate to) {

    List<Booking> bookings =
            new ArrayList<>();

    String sql =
            "SELECT * FROM bookings " +
            "WHERE facility_name = ? " +
            "AND booking_date BETWEEN ? AND ?";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setString(1, facility);

        stmt.setDate(
                2,
                Date.valueOf(from)
        );

        stmt.setDate(
                3,
                Date.valueOf(to)
        );

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            bookings.add(
                    mapBooking(rs)
            );
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to retrieve bookings."
        );
    }

    return bookings;
}
}