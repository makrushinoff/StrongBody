package ua.strongBody.services;

import ua.strongBody.models.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingService extends GeneralService<Booking> {

    void createBooking(Booking booking);

    List<Booking> getCustomerBookingsByCartId(UUID cartId);
}