package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.BookingDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Booking;
import ua.strongBody.processors.post.PostProcessor;
import ua.strongBody.services.BookingService;

import java.util.List;
import java.util.UUID;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingDAO bookingDAO;

    private final PostProcessor<Booking> bookingPostProcessor;

    public BookingServiceImpl(BookingDAO bookingDAO, PostProcessor<Booking> bookingPostProcessor) {
        this.bookingDAO = bookingDAO;
        this.bookingPostProcessor = bookingPostProcessor;
    }

    @Override
    public List<Booking> findAll() {
        LOG.debug(LOG_DEBUG_EMPTY_PATTERN);
        return bookingDAO.findAll();
    }

    @Override
    public void save(Booking booking) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, booking);
        bookingDAO.save(booking);
    }

    @Override
    public void updateById(UUID id, Booking booking) {
        LOG.debug(LOG_DEBUG_TWO_ARG_PATTERN, booking, id);
        bookingDAO.updateById(id, booking);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        bookingDAO.deleteById(id);
    }

    @Override
    public Booking findById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        return bookingDAO.findById(id)
                .orElseThrow(() -> generateGeneralBookingException(Booking.ID_FIELD, id.toString()));
    }

    private RuntimeException generateGeneralBookingException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_PRODUCT_NOT_FOUND_PATTERN, invalidField, invalidValue);
        LOG.warn(message);
        return new FieldNotFoundException(message);
    }

    @Override
    public void createBooking(Booking booking) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, booking);
        bookingDAO.saveWithoutId(booking);
    }

    @Override
    public List<Booking> getCustomerBookingsByCartId(UUID cartId) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, cartId);
        List<Booking> bookings = bookingDAO.getBookingsByCartId(cartId);
        bookingPostProcessor.postProcess(bookings);
        return bookings;
    }
}
