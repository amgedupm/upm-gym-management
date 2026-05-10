package com.upm.gym.service;

import com.upm.gym.dao.BookingDAO;
import com.upm.gym.enums.BookingStatus;
import com.upm.gym.exception.BookingException;
import com.upm.gym.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private final BookingDAO bookingDAO;

    public BookingService() {
        bookingDAO = new BookingDAO();
    }

    public void submitBooking(Booking booking) throws BookingException {
        if (booking.getStartTime().isAfter(booking.getEndTime())) {
            throw new BookingException("Invalid booking time.");
        }
        bookingDAO.createBooking(booking);
    }

    public void approveBooking(int bookingId) {
        bookingDAO.updateBookingStatus(bookingId, BookingStatus.APPROVED);
    }

    public void rejectBooking(int bookingId) {
        bookingDAO.updateBookingStatus(bookingId, BookingStatus.REJECTED);
    }

    /**
     * STUB — returns hardcoded sample bookings for the given user.
     * Real version will query BookingDAO.getBookingsByUser(userId).
     */
    public List<Booking> getBookingsByUser(String userId) {
        List<Booking> fake = new ArrayList<>();

        Booking b1 = new Booking();
        b1.setBookingId(101);
        b1.setUserId(userId);
        b1.setFacilityName("Football Field");
        b1.setBookingDate(LocalDate.now().plusDays(2));
        b1.setStartTime(LocalTime.of(18, 0));
        b1.setEndTime(LocalTime.of(19, 0));
        b1.setStatus(BookingStatus.APPROVED);

        Booking b2 = new Booking();
        b2.setBookingId(102);
        b2.setUserId(userId);
        b2.setFacilityName("Basketball Court");
        b2.setBookingDate(LocalDate.now().plusDays(5));
        b2.setStartTime(LocalTime.of(20, 0));
        b2.setEndTime(LocalTime.of(21, 30));
        b2.setStatus(BookingStatus.PENDING);

        Booking b3 = new Booking();
        b3.setBookingId(103);
        b3.setUserId(userId);
        b3.setFacilityName("Football Field");
        b3.setBookingDate(LocalDate.now().minusDays(3));
        b3.setStartTime(LocalTime.of(17, 0));
        b3.setEndTime(LocalTime.of(18, 0));
        b3.setStatus(BookingStatus.REJECTED);

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

    /**
     * STUB — returns hardcoded sample pending bookings.
     * Real version will query BookingDAO.getPendingBookings().
     */
    public List<Booking> getPendingBookings() {
        List<Booking> fake = new ArrayList<>();

        Booking b1 = new Booking();
        b1.setBookingId(201);
        b1.setUserId("4410097");
        b1.setFacilityName("Football Field");
        b1.setBookingDate(LocalDate.now().plusDays(3));
        b1.setStartTime(LocalTime.of(17, 0));
        b1.setEndTime(LocalTime.of(18, 0));
        b1.setStatus(BookingStatus.PENDING);

        Booking b2 = new Booking();
        b2.setBookingId(202);
        b2.setUserId("4413828");
        b2.setFacilityName("Basketball Court");
        b2.setBookingDate(LocalDate.now().plusDays(4));
        b2.setStartTime(LocalTime.of(20, 0));
        b2.setEndTime(LocalTime.of(21, 30));
        b2.setStatus(BookingStatus.PENDING);

        Booking b3 = new Booking();
        b3.setBookingId(203);
        b3.setUserId("4510353");
        b3.setFacilityName("Football Field");
        b3.setBookingDate(LocalDate.now().plusDays(7));
        b3.setStartTime(LocalTime.of(19, 0));
        b3.setEndTime(LocalTime.of(20, 0));
        b3.setStatus(BookingStatus.PENDING);

        fake.add(b1);
        fake.add(b2);
        fake.add(b3);
        return fake;
    }

    /**
     * STUB — returns hardcoded sample approved bookings for today.
     * Real version will query BookingDAO.getApprovedBookingsByDate(LocalDate.now()).
     */
    public List<Booking> getApprovedBookingsForToday() {
        List<Booking> fake = new ArrayList<>();

        Booking b1 = new Booking();
        b1.setBookingId(301);
        b1.setUserId("4410097");
        b1.setFacilityName("Football Field");
        b1.setBookingDate(LocalDate.now());
        b1.setStartTime(LocalTime.of(16, 0));
        b1.setEndTime(LocalTime.of(17, 0));
        b1.setStatus(BookingStatus.APPROVED);

        Booking b2 = new Booking();
        b2.setBookingId(302);
        b2.setUserId("4413828");
        b2.setFacilityName("Basketball Court");
        b2.setBookingDate(LocalDate.now());
        b2.setStartTime(LocalTime.of(18, 0));
        b2.setEndTime(LocalTime.of(19, 30));
        b2.setStatus(BookingStatus.APPROVED);

        Booking b3 = new Booking();
        b3.setBookingId(303);
        b3.setUserId("4510353");
        b3.setFacilityName("Football Field");
        b3.setBookingDate(LocalDate.now());
        b3.setStartTime(LocalTime.of(20, 0));
        b3.setEndTime(LocalTime.of(21, 0));
        b3.setStatus(BookingStatus.APPROVED);

        fake.add(b1);
        fake.add(b2);
        fake.add(b3);
        return fake;
    }
    /**
     * STUB — returns this user's approved bookings for today.
     * Real version will query BookingDAO.getApprovedBookingsByUserAndDate(userId, LocalDate.now()).
     */
    public java.util.List<com.upm.gym.model.Booking> getApprovedBookingsForUserToday(String userId) {
        java.util.List<com.upm.gym.model.Booking> all = getApprovedBookingsForToday();
        java.util.List<com.upm.gym.model.Booking> filtered = new java.util.ArrayList<>();
        for (com.upm.gym.model.Booking b : all) {
            if (b.getUserId().equals(userId)) {
                filtered.add(b);
            }
        }
        return filtered;
    }
}