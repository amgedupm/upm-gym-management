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
     * STUB — returns hardcoded sample bookings for the given user.
     * Real version will query BookingDAO.getBookingsByUser(userId).
     */
    public java.util.List<com.upm.gym.model.Booking> getBookingsByUser(String userId) {
        java.util.List<com.upm.gym.model.Booking> fake = new java.util.ArrayList<>();

        com.upm.gym.model.Booking b1 = new com.upm.gym.model.Booking();
        b1.setBookingId(101);
        b1.setUserId(userId);
        b1.setFacilityName("Football Field");
        b1.setBookingDate(java.time.LocalDate.now().plusDays(2));
        b1.setStartTime(java.time.LocalTime.of(18, 0));
        b1.setEndTime(java.time.LocalTime.of(19, 0));
        b1.setStatus(com.upm.gym.enums.BookingStatus.APPROVED);

        com.upm.gym.model.Booking b2 = new com.upm.gym.model.Booking();
        b2.setBookingId(102);
        b2.setUserId(userId);
        b2.setFacilityName("Basketball Court");
        b2.setBookingDate(java.time.LocalDate.now().plusDays(5));
        b2.setStartTime(java.time.LocalTime.of(20, 0));
        b2.setEndTime(java.time.LocalTime.of(21, 30));
        b2.setStatus(com.upm.gym.enums.BookingStatus.PENDING);

        com.upm.gym.model.Booking b3 = new com.upm.gym.model.Booking();
        b3.setBookingId(103);
        b3.setUserId(userId);
        b3.setFacilityName("Football Field");
        b3.setBookingDate(java.time.LocalDate.now().minusDays(3));
        b3.setStartTime(java.time.LocalTime.of(17, 0));
        b3.setEndTime(java.time.LocalTime.of(18, 0));
        b3.setStatus(com.upm.gym.enums.BookingStatus.REJECTED);

        fake.add(b1);
        fake.add(b2);
        fake.add(b3);
        return fake;
    }

    /**
     * STUB — pretends to cancel a booking.
     * Real version will UPDATE the bookings row, setting status to CANCELLED.
     */
    public boolean cancelBooking(int bookingId) {
        System.out.println("[STUB] Cancelled booking #" + bookingId);
        return true;
    }
}
