package ua.strongBody.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.strongBody.assembly.BookingAssembly;
import ua.strongBody.assembly.CartAssembly;
import ua.strongBody.assembly.ProductAssembly;
import ua.strongBody.dao.impl.BookingDAOImpl;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Product;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingDAOImplUnitTest {

    private static final String SAVE_QUERY = "INSERT INTO booking(id, order_date, product_amount, order_number, product_id, cart_id)" +
            " VALUES('%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String UPDATE_QUERY = "UPDATE booking SET " +
            "order_date = '%s'," +
            "product_amount = '%s'," +
            "order_number = '%s'," +
            "product_id = '%s' " +
            "WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM booking WHERE id = '%s'";
    private static final String SAVE_WITHOUT_ID_QUERY = "INSERT INTO booking(order_date, product_amount, order_number, product_id, cart_id)" +
            " VALUES('%s', '%s', '%s', '%s', '%s')";

    private static final LocalDate ORDER_DATE = LocalDate.of(2021, 10, 28);
    private static final int PRODUCT_AMOUNT = 10;
    private static final String ORDER_NUMBER = "12345";
    private static final String PRODUCT_NAME = "prod1";
    private static final int PRICE = 2000;
    private static final String ARTICLE = "PP-01";
    private static final String DESCRIPTION = "Super-puper product1";
    private static final int AVAILABLE_AMOUNT = 5;
    private static final String EMAIL = "cus@bla.com";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String PHONE_NUMBER = "+99999999";
    private static final UUID CART_ID = UUID.randomUUID();
    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID BOOKING_ID = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();

    private Booking booking;
    private Product product;
    private Cart cart;
    private Customer customer;
    private List<Booking> bookingList;
    private List<Product> productList;
    private List<Cart> cartList;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private BookingAssembly bookingAssembly;

    @Mock
    private ProductAssembly productAssembly;

    @Mock
    private CartAssembly cartAssembly;

    @InjectMocks
    private BookingDAOImpl testInstance;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(PRICE);
        product.setArticle(ARTICLE);
        product.setDescription(DESCRIPTION);
        product.setAmount(AVAILABLE_AMOUNT);

        customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setEmail(EMAIL);
        customer.setUsername(USERNAME);
        customer.setPassword(PASSWORD);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setPhoneNumber(PHONE_NUMBER);

        cart = new Cart();
        cart.setId(CART_ID);
        cart.setCustomer(customer);

        booking = new Booking();
        booking.setId(BOOKING_ID);
        booking.setOrderDate(ORDER_DATE);
        booking.setProductAmount(PRODUCT_AMOUNT);
        booking.setOrderNumber(ORDER_NUMBER);
        booking.setProduct(product);
        booking.setCart(cart);

        bookingList = Collections.singletonList(booking);
        productList = Collections.singletonList(product);
        cartList = Collections.singletonList(cart);
    }

    @Test
    void shouldFindAll() {
        when(bookingAssembly.findAllSingleLayer()).thenReturn(bookingList);
        when(productAssembly.findAllProduct()).thenReturn(productList);
        when(cartAssembly.findAllCartsSingleLayer()).thenReturn(cartList);

        List<Booking> actualList = testInstance.findAll();
        Optional<Booking> actualOptional = actualList.stream().findFirst();

        assertThat(actualOptional).isPresent();
        Booking actual = actualOptional.get();
        assertThat(actual).isEqualTo(booking);
        assertThat(actual.getProduct()).isEqualTo(product);
        assertThat(actual.getCart()).isEqualTo(cart);
    }

    @Test
    void shouldSave() {
        testInstance.save(booking);

        verify(jdbcTemplate).update(String.format(SAVE_QUERY,
                BOOKING_ID,
                ORDER_DATE,
                PRODUCT_AMOUNT,
                ORDER_NUMBER,
                PRODUCT_ID,
                CART_ID)
        );
    }

    @Test
    void shouldSaveWithoutId() {
        testInstance.saveWithoutId(booking);

        verify(jdbcTemplate).update(String.format(SAVE_WITHOUT_ID_QUERY,
                ORDER_DATE,
                PRODUCT_AMOUNT,
                ORDER_NUMBER,
                PRODUCT_ID,
                CART_ID)
        );
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(BOOKING_ID, booking);

        verify(jdbcTemplate).update(String.format(UPDATE_QUERY,
                ORDER_DATE,
                PRODUCT_AMOUNT,
                ORDER_NUMBER,
                PRODUCT_ID,
                BOOKING_ID)
        );
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(BOOKING_ID);

        verify(jdbcTemplate).update(String.format(DELETE_QUERY, BOOKING_ID));
    }

    @Test
    void shouldFindById() {
        when(bookingAssembly.findAllSingleLayer()).thenReturn(bookingList);

        Optional<Booking> actualOptional = testInstance.findById(BOOKING_ID);

        assertThat(actualOptional).isPresent();
        Booking actual = actualOptional.get();
        assertThat(actual).isEqualTo(bookingList.get(0));
    }

    @Test
    void shouldGetBookingsByCartId() {
        when(bookingAssembly.findAllSingleLayer()).thenReturn(bookingList);

        List<Booking> actual = testInstance.getBookingsByCartId(CART_ID);

        assertThat(bookingList).isEqualTo(actual);
        assertThat(actual.get(0)).isEqualTo(bookingList.get(0));
    }
}
