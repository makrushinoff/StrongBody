package ua.strongBody.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.strongBody.assembly.CartAssembly;
import ua.strongBody.assembly.CustomerAssembly;
import ua.strongBody.dao.impl.CartDAOImpl;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartDAOImplUnitTest {

    private static final String SAVE_QUERY = "INSERT INTO cart (id, customer_id) VALUES ('%s' , '%s')";
    private static final String SAVE_WITHOUT_ID_QUERY = "INSERT INTO cart (customer_id) VALUES ('%s')";
    private static final String UPDATE_QUERY = "UPDATE cart SET customer_id = '%s' WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM cart WHERE id = '%s'";

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String EMAIL = "cus@bla.com";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String PHONE_NUMBER = "+99999999";

    private static final UUID CART_ID = UUID.randomUUID();

    private Customer customer;
    private Cart cart;
    private List<Cart> cartList;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private CartAssembly cartAssembly;

    @Mock
    private CustomerAssembly customerAssembly;

    @InjectMocks
    private CartDAOImpl testInstance;

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

        cartList = Collections.singletonList(cart);
    }

    @Test
    void shouldSave() {
        testInstance.save(cart);

        verify(jdbcTemplate).update(String.format(SAVE_QUERY, CART_ID, CUSTOMER_ID));
    }

    @Test
    void shouldSaveWithoutId() {
        testInstance.saveWithoutId(cart);

        verify(jdbcTemplate).update(String.format(SAVE_WITHOUT_ID_QUERY, CUSTOMER_ID));
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(CART_ID, cart);

        verify(jdbcTemplate).update(String.format(UPDATE_QUERY, CUSTOMER_ID, CART_ID));
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(CART_ID);

        verify(jdbcTemplate).update(String.format(DELETE_QUERY, CART_ID));
    }

    @Test
    void shouldFindCartByCustomerId() {
        when(cartAssembly.findAllCartsSingleLayer()).thenReturn(cartList);

        Optional<Cart> actualOptional = testInstance.findCartByCustomerId(CUSTOMER_ID);

        assertThat(actualOptional).isPresent();
        Cart actual = actualOptional.get();
        assertThat(actual).isEqualTo(cartList.get(0));
    }

    @Test
    void shouldFindById() {
        when(cartAssembly.findAllCartsSingleLayer()).thenReturn(cartList);

        Optional<Cart> actualOptional = testInstance.findById(CART_ID);

        assertThat(actualOptional).isPresent();
        Cart actual = actualOptional.get();
        assertThat(actual).isEqualTo(cartList.get(0));
    }
}
