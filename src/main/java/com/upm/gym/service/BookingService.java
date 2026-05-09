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

    /**
     * STUB — returns hardcoded sample pending bookings.
     * Real version will query BookingDAO.getPendingBookings()
     * which fetches all bookings WHERE status = 'PENDING'.
     */
    public java.util.List<com.upm.gym.model.Booking> getPendingBookings() {
        java.util.List<com.upm.gym.model.Booking> fake = new java.util.ArrayList<>();

        com.upm.gym.model.Booking b1 = new com.upm.gym.model.Booking();
        b1.setBookingId(201);
        b1.setUserId("4410097");
        b1.setFacilityName("Football Field");
        b1.setBookingDate(java.time.LocalDate.now().plusDays(3));
        b1.setStartTime(java.time.LocalTime.of(17, 0));
        b1.setEndTime(java.time.LocalTime.of(18, 0));
        b1.setStatus(com.upm.gym.enums.BookingStatus.PENDING);

        com.upm.gym.model.Booking b2 = new com.upm.gym.model.Booking();
        b2.setBookingId(202);
        b2.setUserId("4413828");
        b2.setFacilityName("Basketball Court");
        b2.setBookingDate(java.time.LocalDate.now().plusDays(4));
        b2.setStartTime(java.time.LocalTime.of(20, 0));
        b2.setEndTime(java.time.LocalTime.of(21, 30));
        b2.setStatus(com.upm.gym.enums.BookingStatus.PENDING);

        com.upm.gym.model.Booking b3 = new com.upm.gym.model.Booking();
        b3.setBookingId(203);
        b3.setUserId("4510353");
        b3.setFacilityName("Football Field");
        b3.setBookingDate(java.time.LocalDate.now().plusDays(7));
        b3.setStartTime(java.time.LocalTime.of(19, 0));
        b3.setEndTime(java.time.LocalTime.of(20, 0));
        b3.setStatus(com.upm.gym.enums.BookingStatus.PENDING);

        fake.add(b1);
        fake.add(b2);
        fake.add(b3);
        return fake;
    }
}
