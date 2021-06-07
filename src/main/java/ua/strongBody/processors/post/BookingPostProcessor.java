package ua.strongBody.processors.post;

import org.springframework.stereotype.Component;
import ua.strongBody.models.Booking;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
public class BookingPostProcessor implements PostProcessor<Booking> {

    @Override
    public void postProcess(List<Booking> bookings) {
        sortBookings(bookings);
        bookings.forEach(this::postProcess);
    }

    @Override
    public void postProcess(Booking booking) {
        calculateBookingPrice(booking);
    }

    private void calculateBookingPrice(Booking booking) {
        BigDecimal productPrice = booking.getProduct().getPrice();
        int productAmount = booking.getProductAmount();
        BigDecimal bookingPrice = productPrice.multiply(BigDecimal.valueOf(productAmount));
        booking.setPrice(bookingPrice);
    }

    private void sortBookings(List<Booking> bookings) {
        bookings.sort(Comparator.comparing(Booking::getId).reversed());
    }
}
