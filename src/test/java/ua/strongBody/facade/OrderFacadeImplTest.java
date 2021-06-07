package ua.strongBody.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.facade.impl.OrderFacadeImpl;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Order;
import ua.strongBody.populator.Populator;
import ua.strongBody.services.CartService;
import ua.strongBody.services.CustomerService;
import ua.strongBody.services.OrderService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderFacadeImplTest {

    private static final UUID CART_ID = UUID.randomUUID();
    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String EMAIL = "cus@bla.com";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String PHONE_NUMBER = "+99999999";

    private Customer customer;
    private Cart cart;

    @Mock
    private CartService cartService;

    @Mock
    private CustomerService customerService;

    @Mock
    private OrderService orderService;

    @Mock
    private Populator<Cart, Order> cartOrderPopulator;

    @InjectMocks
    private OrderFacadeImpl testInstance;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void shouldCreateOrderFromCartByCustomerUsername() {
        when(customerService.findByUsername(USERNAME)).thenReturn(customer);
        when(cartService.findCartByCustomerId(CUSTOMER_ID)).thenReturn(cart);

        testInstance.createOrderFromCartByCustomerUsername(USERNAME);

        verify(cartOrderPopulator).convert(eq(cart), any(Order.class));
        verify(orderService).save(any(Order.class));
    }

    @Test
    void shouldGetOrdersByCustomerUsername() {
        when(customerService.findByUsername(USERNAME)).thenReturn(customer);

        testInstance.getOrdersByCustomerUsername(USERNAME);

        verify(orderService).findOrdersByCustomerId(CUSTOMER_ID);
    }
}
