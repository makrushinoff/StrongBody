package ua.strongBody.processors.post;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BookingPostProcessorTest {

    private final BookingPostProcessor testInstance = new BookingPostProcessor();

    @ParameterizedTest
    @MethodSource("calculateBookingPriceTestData")
    void shouldCalculateBookingPrice(Booking booking, BigDecimal expectedPrice) {
        testInstance.postProcess(booking);

        assertThat(booking.getPrice()).isEqualTo(expectedPrice);
    }

    @ParameterizedTest
    @MethodSource("sortBookingsTestData")
    void shouldSortBookings(List<Booking> input, List<Booking> expected) {
        testInstance.postProcess(input);

        assertThat(input).isEqualTo(expected);
    }

    private static Stream<Arguments> calculateBookingPriceTestData() {
        Product product10 = createProductByPrice(10);
        Product product25 = createProductByPrice(25);
        Product product50 = createProductByPrice(50);

        Booking booking1 = createBookingByProductAndAmount(product10, 20);
        Booking booking2 = createBookingByProductAndAmount(product25, 50);
        Booking booking3 = createBookingByProductAndAmount(product50, 100);

        return Stream.of(
                Arguments.of(booking1, BigDecimal.valueOf(200)),
                Arguments.of(booking2, BigDecimal.valueOf(1250)),
                Arguments.of(booking3, BigDecimal.valueOf(5000))
        );
    }

    private static Stream<Arguments> sortBookingsTestData() {
        Booking booking1 = createBookingById();
        Booking booking2 = createBookingById();
        Booking booking3 = createBookingById();

        List<Booking> expectedList = Arrays.asList(booking1, booking2, booking3);
        expectedList.sort(Comparator.comparing(Booking::getId));

        return Stream.of(
                Arguments.of(Arrays.asList(booking1, booking2, booking3), expectedList),
                Arguments.of(Arrays.asList(booking2, booking3, booking1), expectedList),
                Arguments.of(Arrays.asList(booking3, booking1, booking2), expectedList)
        );

    }

    private static Product createProductByPrice(int price) {
        Product product = new Product();
        product.setPrice(price);
        return product;
    }

    private static Booking createBookingByProductAndAmount(Product product, int productAmount) {
        Booking booking = new Booking();
        booking.setProduct(product);
        booking.setProductAmount(productAmount);
        return booking;
    }

    private static Booking createBookingById() {
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setProduct(new Product());
        return booking;
    }
}
