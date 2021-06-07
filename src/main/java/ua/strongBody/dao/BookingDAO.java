package ua.strongBody.dao;

import ua.strongBody.models.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingDAO extends GeneralDAO<Booking> {

    void saveWithoutId(Booking booking);

    List<Booking> getBookingsByCartId(UUID cartId);
}
