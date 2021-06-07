package ua.strongBody.processors.post;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.services.BookingService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartPostProcessorTest {

    private static final UUID CART_ID = UUID.randomUUID();

    private final Cart cart = new Cart();

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private CartPostProcessor testInstance;

    @ParameterizedTest
    @MethodSource("calculateQuantityTestData")
    void shouldCalculateQuantity(List<Booking> bookingList, int expectedQuantity) {
        cart.setBookingList(bookingList);

        testInstance.postProcess(cart);

        assertThat(cart.getQuantity()).isEqualTo(expectedQuantity);
    }

    @ParameterizedTest
    @MethodSource("calculatePriceTestData")
    void shouldCalculatePrice(List<Booking> bookingList, BigDecimal expectedPrice) {
        cart.setId(CART_ID);
        cart.setBookingList(bookingList);
        when(bookingService.getCustomerBookingsByCartId(CART_ID)).thenReturn(bookingList);

        testInstance.postProcess(cart);

        assertThat(cart.getPrice()).isEqualTo(expectedPrice);
    }

    @ParameterizedTest
    @MethodSource("shouldProcessCartListTestData")
    void shouldProcessCartList(Cart cart, int expectedQuantity, BigDecimal expectedPrice) {
        when(bookingService.getCustomerBookingsByCartId(cart.getId())).thenReturn(cart.getBookingList());

        testInstance.postProcess(Collections.singletonList(cart));

        assertThat(cart.getQuantity()).isEqualTo(expectedQuantity);
        assertThat(cart.getPrice()).isEqualTo(expectedPrice);
    }

    private static Stream<Arguments> calculateQuantityTestData() {
        Booking booking10 = createBookingByProductAmount(10);
        Booking booking20 = createBookingByProductAmount(20);
        Booking booking40 = createBookingByProductAmount(40);
        Booking booking80 = createBookingByProductAmount(80);

        return Stream.of(
                Arguments.of(Arrays.asList(booking10, booking80), 90),
                Arguments.of(Arrays.asList(booking20, booking80), 100),
                Arguments.of(Collections.singletonList(booking80), 80),
                Arguments.of(Collections.emptyList(), 0),
                Arguments.of(Arrays.asList(booking10, booking40, booking20, booking80), 150)
        );
    }

    private static Stream<Arguments> calculatePriceTestData() {
        Booking booking10_50 = createBookingByPrice(BigDecimal.valueOf(10.5));
        Booking booking20 = createBookingByPrice(BigDecimal.valueOf(20));
        Booking booking12_50 = createBookingByPrice(BigDecimal.valueOf(12.5));
        Booking booking67 = createBookingByPrice(BigDecimal.valueOf(67));

        return Stream.of(
                Arguments.of(Collections.emptyList(), BigDecimal.valueOf(0)),
                Arguments.of(Collections.singletonList(booking12_50), BigDecimal.valueOf(12.50)),
                Arguments.of(Arrays.asList(booking10_50, booking12_50), BigDecimal.valueOf(23.0)),
                Arguments.of(Arrays.asList(booking10_50, booking20, booking12_50, booking67), BigDecimal.valueOf(110.0))
        );
    }

    private static Stream<Arguments> shouldProcessCartListTestData() {
        Booking booking1 = createBookingByQuantityAndPrice(10, BigDecimal.valueOf(225));
        Booking booking2 = createBookingByQuantityAndPrice(20, BigDecimal.valueOf(500));
        Booking booking3 = createBookingByQuantityAndPrice(50, BigDecimal.valueOf(150));
        Cart cart1 = createCartByBookingList(Arrays.asList(booking1, booking2, booking3));
        Cart cart2 = createCartByBookingList(Collections.emptyList());
        Cart cart3 = createCartByBookingList(Collections.singletonList(booking3));

        return Stream.of(
                Arguments.of(cart1, 80, BigDecimal.valueOf(875)),
                Arguments.of(cart2, 0, BigDecimal.ZERO),
                Arguments.of(cart3, 50, BigDecimal.valueOf(150))
        );

    }

    private static Booking createBookingByProductAmount(int productAmount) {
        Booking booking = new Booking();
        booking.setProductAmount(productAmount);
        return booking;
    }

    private static Booking createBookingByPrice(BigDecimal price) {
        Booking booking = new Booking();
        booking.setPrice(price);
        return booking;
    }

    private static Cart createCartByBookingList(List<Booking> bookingList) {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setBookingList(bookingList);
        return cart;
    }

    private static Booking createBookingByQuantityAndPrice(int productAmount, BigDecimal price) {
        Booking booking = new Booking();
        booking.setProductAmount(productAmount);
        booking.setPrice(price);
        return booking;
    }
}
