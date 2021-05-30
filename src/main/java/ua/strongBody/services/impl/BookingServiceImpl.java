package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.BookingDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Booking;
import ua.strongBody.services.BookingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingDAO bookingDAO;

    public BookingServiceImpl(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
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
        Optional<Booking> bookingOptional = bookingDAO.findById(id);
        return processInstanceExport(bookingOptional, id.toString(), Booking.ID_FIELD);
    }

    private Booking processInstanceExport(Optional<Booking> bookingOptional, String requestedValue, String fieldName) {
        if (bookingOptional.isPresent()) {
            return bookingOptional.get();
        }
        processGeneralBookingException(fieldName, requestedValue);
        return null;
    }

    private void processGeneralBookingException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_PRODUCT_NOT_FOUND_PATTERN, invalidField, invalidValue);
        LOG.warn(message);
        throw new FieldNotFoundException(message);
    }

    @Override
    public void createBooking(Booking booking) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, booking);
        bookingDAO.saveWithoutId(booking);
    }

    @Override
    public List<Booking> getCustomerBookingsByCartId(UUID cartId) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, cartId);
        return bookingDAO.getBookingsByCartId(cartId);
    }
}
