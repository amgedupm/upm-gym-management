package com.upm.gym.service;

import com.upm.gym.dao.BookingDAO;
import com.upm.gym.enums.BookingStatus;
import com.upm.gym.exception.BookingException;
import com.upm.gym.model.Booking;

public class BookingService {

    private final BookingDAO bookingDAO;

    public BookingService() {
        bookingDAO = new BookingDAO();
    }

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

        bookingDAO.createBooking(booking);
    }

    public void approveBooking(
            int bookingId) {

        bookingDAO.updateBookingStatus(
                bookingId,
                BookingStatus.APPROVED
        );
    }

    public void rejectBooking(
            int bookingId) {

        bookingDAO.updateBookingStatus(
                bookingId,
                BookingStatus.REJECTED
        );
    }
}
