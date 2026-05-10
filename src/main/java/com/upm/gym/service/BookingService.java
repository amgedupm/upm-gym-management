package com.upm.gym.service;

import com.upm.gym.dao.BookingDAO;
import com.upm.gym.enums.BookingStatus;
import com.upm.gym.exception.BookingException;
import com.upm.gym.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BookingService {

    private final BookingDAO bookingDAO;

    public BookingService() {
        bookingDAO = new BookingDAO();
    }

    /**
     * Submit a new booking request.
     */
    public void submitBooking(
            Booking booking)
            throws BookingException {

        if (booking.getStartTime()
                .isAfter(
                        booking.getEndTime())) {

            throw new BookingException(
                    "Invalid booking time."
            );
        }

        bookingDAO.createBooking(
                booking
        );
    }

    /**
     * Approve a booking request.
     */
    public void approveBooking(
            int bookingId) {

        bookingDAO.updateBookingStatus(
                bookingId,
                BookingStatus.APPROVED
        );
    }

    /**
     * Reject a booking request.
     */
    public void rejectBooking(
            int bookingId) {

        bookingDAO.updateBookingStatus(
                bookingId,
                BookingStatus.REJECTED
        );
    }

    /**
     * Retrieve all bookings for a user.
     */
    public List<Booking> getBookingsByUser(
            String userId) {

        return bookingDAO
                .getBookingsByUser(
                        userId
                );
    }

    /**
     * Cancel a booking.
     */
    public boolean cancelBooking(
            int bookingId) {

        return bookingDAO
                .cancelBooking(
                        bookingId
                );
    }

    /**
     * Retrieve all pending bookings.
     */
    public List<Booking> getPendingBookings() {

        return bookingDAO
                .getPendingBookings();
    }

    /**
     * Retrieve today's approved bookings.
     */
    public List<Booking>
    getApprovedBookingsForToday() {

        return bookingDAO
                .getApprovedBookingsByDate(
                        LocalDate.now()
                );
    }

    /**
     * Retrieve today's approved bookings
     * for a specific user.
     */
    public List<Booking>
    getApprovedBookingsForUserToday(
            String userId) {

        return bookingDAO
                .getApprovedBookingsByUserAndDate(
                        userId,
                        LocalDate.now()
                );
    }

    /**
     * Count all bookings for today.
     */
    public int countBookingsToday() {

        return bookingDAO
                .countBookingsToday();
    }

    /**
     * Count pending booking requests.
     */
    public int countPendingBookings() {

        return bookingDAO
                .countPendingBookings();
    }

    /**
     * Count bookings grouped by facility
     * for the current month.
     */
    public Map<String, Integer>
    countBookingsByFacilityThisMonth() {

        return bookingDAO
                .countBookingsByFacilityThisMonth();
    }
}