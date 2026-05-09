package com.upm.gym;

import com.upm.gym.enums.BookingStatus;
import com.upm.gym.exception.BookingException;
import com.upm.gym.model.Booking;
import com.upm.gym.service.BookingService;

import java.time.LocalDate;
import java.time.LocalTime;

public class BackendTest {

    static void main() {

        // Create service object
        BookingService bookingService =
                new BookingService();

        // Create booking object
        Booking booking = new Booking(
                0,
                "4410097",
                "Football Field",
                LocalDate.now(),
                LocalTime.of(5, 0),
                LocalTime.of(7, 0),
                BookingStatus.PENDING
        );

        try {

            // Submit booking
            bookingService.submitBooking(booking);

            System.out.println(
                    "Booking submitted successfully."
            );

        } catch (BookingException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }
}
