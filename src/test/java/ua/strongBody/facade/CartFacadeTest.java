package ua.strongBody.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Product;
import ua.strongBody.services.BookingService;
import ua.strongBody.services.CartService;
import ua.strongBody.services.CustomerService;
import ua.strongBody.services.ProductService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartFacadeTest {

    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID CART_ID = UUID.randomUUID();
    private static final UUID BOOKING_ID = UUID.randomUUID();
    private static final String USERNAME = "username";
    private static final int PRODUCT_AMOUNT = 5;
    private static final int AMOUNT = 10;
    private static final int RESERVED_AMOUNT = 5;

    private Customer customer;
    private Cart cart;
    private Product product;
    private Booking booking;

    @Mock
    private CartService cartService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private BookingService bookingService;

    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;

    @InjectMocks
    private CartFacadeImpl testInstance;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(CUSTOMER_ID);

        cart = new Cart();
        cart.setId(CART_ID);

        product = new Product();
        product.setId(PRODUCT_ID);
        product.setReservedAmount(RESERVED_AMOUNT);
        product.setAmount(AMOUNT);

        booking = new Booking();
        booking.setId(BOOKING_ID);
        booking.setProduct(product);
        booking.setProductAmount(PRODUCT_AMOUNT);
    }

    @Test
    void shouldAddProductToCartWithExistingBooking() {
        when(customerService.findByUsername(USERNAME)).thenReturn(customer);
        when(cartService.findCartByCustomerId(CUSTOMER_ID)).thenReturn(cart);
        when(productService.findById(PRODUCT_ID)).thenReturn(product);
        when(bookingService.getCustomerBookingsByCartId(CART_ID)).thenReturn(Collections.singletonList(booking));

        testInstance.addProductToCartByCustomerUsername(PRODUCT_ID, USERNAME);

        assertThat(booking.getProductAmount()).isEqualTo(6);
        assertThat(product.getReservedAmount()).isEqualTo(6);
        verify(bookingService).updateById(BOOKING_ID, booking);
        verify(productService).updateById(PRODUCT_ID, product);
    }

    @Test
    void shouldAddProductToCartWithNewBookingCreation() {
        when(customerService.findByUsername(USERNAME)).thenReturn(customer);
        when(cartService.findCartByCustomerId(CUSTOMER_ID)).thenReturn(cart);
        when(productService.findById(PRODUCT_ID)).thenReturn(product);
        when(bookingService.getCustomerBookingsByCartId(CART_ID)).thenReturn(Collections.emptyList());

        testInstance.addProductToCartByCustomerUsername(PRODUCT_ID, USERNAME);

        verify(bookingService).createBooking(bookingCaptor.capture());
        Booking actualNewBooking = bookingCaptor.getValue();
        assertThat(actualNewBooking.getProduct()).isEqualTo(product);
        assertThat(actualNewBooking.getCart()).isEqualTo(cart);
        assertThat(actualNewBooking.getProductAmount()).isEqualTo(1);
        assertThat(actualNewBooking.getOrderNumber()).isNotNull();
        assertThat(actualNewBooking.getOrderDate()).isNotNull();

        assertThat(product.getReservedAmount()).isEqualTo(6);
        verify(productService).updateById(PRODUCT_ID, product);
    }

    @Test
    void shouldCompletelyRemoveBookingFromCart() {
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);
        booking.setProductAmount(1);

        testInstance.removeBookingFromCart(BOOKING_ID);

        verify(bookingService).deleteById(BOOKING_ID);
        assertThat(product.getReservedAmount()).isEqualTo(4);
        verify(productService).updateById(PRODUCT_ID, product);
    }

    @Test
    void shouldRemoveBookingFromCart() {
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);

        testInstance.removeBookingFromCart(BOOKING_ID);

        assertThat(booking.getProductAmount()).isEqualTo(4);
        assertThat(product.getReservedAmount()).isEqualTo(4);
        verify(bookingService, never()).deleteById(BOOKING_ID);
        verify(bookingService).updateById(BOOKING_ID, booking);
        verify(productService).updateById(PRODUCT_ID, product);
    }

    @Test
    void shouldSubmitCartByCustomerUsername() {
        when(customerService.findByUsername(USERNAME)).thenReturn(customer);
        when(cartService.findCartByCustomerId(CUSTOMER_ID)).thenReturn(cart);
        when(bookingService.getCustomerBookingsByCartId(CART_ID)).thenReturn(Collections.singletonList(booking));

        testInstance.submitCartByCustomerUsername(USERNAME);

        verify(bookingService).deleteById(BOOKING_ID);
        assertThat(product.getReservedAmount()).isZero();
        assertThat(product.getAmount()).isEqualTo(5);
        verify(productService).updateById(PRODUCT_ID, product);
    }

    @Test
    void shouldGetCustomerBookingsByUsername() {
        when(customerService.findByUsername(USERNAME)).thenReturn(customer);
        when(cartService.findCartByCustomerId(CUSTOMER_ID)).thenReturn(cart);
        when(bookingService.getCustomerBookingsByCartId(CART_ID)).thenReturn(Collections.singletonList(booking));

        List<Booking> actual = testInstance.getCustomerBookingsByUsername(USERNAME);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual).contains(booking);
    }
}
