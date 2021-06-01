package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.BookingDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Product;
import ua.strongBody.processors.post.PostProcessor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private static final UUID ID = UUID.randomUUID();

    private Booking booking;
    private List<Booking> bookingList;

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private PostProcessor<Booking> bookingPostProcessor;

    @InjectMocks
    private BookingServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setProduct(new Product());
        booking.setCart(new Cart());

        bookingList = Collections.singletonList(booking);
    }

    @Test
    void shouldFindAll() {
        testInstance.findAll();

        verify(bookingDAO).findAll();
    }

    @Test
    void shouldSave() {
        testInstance.save(booking);

        verify(bookingDAO).save(booking);
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ID, booking);

        verify(bookingDAO).updateById(ID, booking);
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ID);

        verify(bookingDAO).deleteById(ID);
    }

    @Test
    void shouldFindById() {
        Optional<Booking> bookingOptional = Optional.of(booking);
        when(bookingDAO.findById(ID)).thenReturn(bookingOptional);

        Booking actual = testInstance.findById(ID);

        assertThat(actual).isEqualTo(booking);
    }

    @Test
    void shouldProcessExportExceptionOnInvalidId() {
        when(bookingDAO.findById(ID)).thenReturn(Optional.empty());

        assertThrows(FieldNotFoundException.class, () -> testInstance.findById(ID));
    }

    @Test
    void shouldCreateBooking() {
        testInstance.createBooking(booking);

        verify(bookingDAO).saveWithoutId(booking);
    }

    @Test
    void shouldGetCustomerBookingsByCartId() {
        when(bookingDAO.getBookingsByCartId(ID)).thenReturn(bookingList);

        testInstance.getCustomerBookingsByCartId(ID);

        verify(bookingDAO).getBookingsByCartId(ID);
        verify(bookingPostProcessor).postProcess(bookingList);
    }
}
