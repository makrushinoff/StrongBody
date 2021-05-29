package ua.strongBody.services.impl;

import org.springframework.stereotype.Service;
import ua.strongBody.dao.BookingDAO;
import ua.strongBody.models.Booking;
import ua.strongBody.services.BookingService;

import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;

    public BookingServiceImpl(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Override
    public List<Booking> findAll() {
        return bookingDAO.findAll();
    }

    @Override
    public void save(Booking booking) {
        bookingDAO.save(booking);
    }

    @Override
    public void updateById(UUID id, Booking booking) {
        bookingDAO.updateById(id, booking);
    }

    @Override
    public void deleteById(UUID id) {
        bookingDAO.deleteById(id);
    }

    @Override
    public Booking findById(UUID id) {
        return bookingDAO.findById(id).orElse(null);
    }

    @Override
    public void createBooking(Booking booking) {
        bookingDAO.saveWithoutId(booking);
    }

    @Override
    public List<Booking> getCustomerBookingsByCartId(UUID cartId) {
        return bookingDAO.getBookingsByCartId(cartId);
    }
}
