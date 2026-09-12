package com.smartbooking.booking.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.smartbooking.booking.entity.Booking;
import com.smartbooking.booking.entity.BookingStatus;
import com.smartbooking.booking.repository.BookingRepository;

@Component
public class BookingCompletionScheduler {

    private final BookingRepository bookingRepository;

    public BookingCompletionScheduler(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Scheduled(cron = "0 0 1 * * *")
    // @Scheduled(fixedDelay = 60000)// 60 seconds
    @Transactional
    public void completeExpiredBookings() {

        LocalDate today = LocalDate.now();

        List<Booking> bookings =
                bookingRepository.findBookingsForCompletion(
                        BookingStatus.CONFIRMED,
                        today
                );

        for (Booking booking : bookings) {
            booking.setStatus(BookingStatus.COMPLETED);
        }

        bookingRepository.saveAll(bookings);
    }
}