package ua.strongBody.processors.post;

import org.springframework.stereotype.Component;
import ua.strongBody.models.Booking;

import java.util.Comparator;
import java.util.List;

@Component
public class BookingPostProcessor implements PostProcessor<Booking> {
    @Override
    public void postProcess(List<Booking> bookings) {
        bookings.sort(Comparator.comparing(Booking::getId).reversed());
    }

    @Override
    public void postProcess(Booking booking) {

    }
}
