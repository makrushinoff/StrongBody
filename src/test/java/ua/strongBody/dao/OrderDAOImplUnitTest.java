package ua.strongBody.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.strongBody.assembly.CustomerAssembly;
import ua.strongBody.assembly.OrderAssembly;
import ua.strongBody.dao.impl.OrderDAOImpl;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDAOImplUnitTest {

    private static final String SAVE_QUERY = "INSERT INTO order_table (order_date, product_amount, price, customer_id)" +
            " VALUES('%s', '%s', '%s', '%s')";
    private static final String UPDATE_QUERY = "UPDATE order_table SET " +
            "order_date = '%s'," +
            "product_amount = '%s'," +
            "price = '%s' WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM order_table WHERE id = '%s'";

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final String EMAIL = "cus@bla.com";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String PHONE_NUMBER = "+99999999";
    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final LocalDate ORDERED_DATE = LocalDate.now();
    private static final int PRODUCT_AMOUNT = 10;
    private static final BigDecimal PRICE = BigDecimal.TEN;

    private Customer customer;
    private Order order;
    private List<Customer> customerList;
    private List<Order> orderList;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private OrderAssembly orderAssembly;

    @Mock
    private CustomerAssembly customerAssembly;

    @InjectMocks
    private OrderDAOImpl testInstance;

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

        customerList = Collections.singletonList(customer);

        order = new Order();
        order.setId(ORDER_ID);
        order.setOrderedDate(ORDERED_DATE);
        order.setProductAmount(PRODUCT_AMOUNT);
        order.setPrice(PRICE);
        order.setCustomer(customer);

        orderList = Collections.singletonList(order);
    }

    @Test
    void shouldFindAll() {
        Customer simpleCustomer = new Customer();
        simpleCustomer.setId(CUSTOMER_ID);
        order.setCustomer(simpleCustomer);
        when(orderAssembly.findAllOrdersSingleLayer()).thenReturn(orderList);
        when(customerAssembly.findAllCustomers()).thenReturn(customerList);

        List<Order> actualList = testInstance.findAll();
        Optional<Order> actualOptional = actualList.stream().findFirst();

        assertThat(actualOptional).isPresent();
        Order actual = actualOptional.get();
        assertThat(actual).isEqualTo(order);
        assertThat(actual.getCustomer()).isEqualTo(customer);
    }

    @Test
    void shouldProcessCustomerUnresolvedWarning() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(UUID.randomUUID());
        order.setCustomer(anotherCustomer);
        when(orderAssembly.findAllOrdersSingleLayer()).thenReturn(orderList);
        when(customerAssembly.findAllCustomers()).thenReturn(customerList);

        List<Order> actualList = testInstance.findAll();
        Optional<Order> actualOptional = actualList.stream().findFirst();

        assertThat(actualOptional).isPresent();
        Order actual = actualOptional.get();
        assertThat(actual).isEqualTo(order);
        assertThat(actual.getCustomer()).isNotEqualTo(customer);
    }

    @Test
    void shouldSave() {
        testInstance.save(order);

        String query = String.format(SAVE_QUERY, order.getOrderedDate(), order.getProductAmount(), order.getPrice(), order.getCustomer().getId());
        verify(jdbcTemplate).update(query);
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ORDER_ID, order);

        String query = String.format(UPDATE_QUERY, order.getOrderedDate(), order.getProductAmount(), order.getPrice(), order.getId());
        verify(jdbcTemplate).update(query);
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ORDER_ID);

        String query = String.format(DELETE_QUERY, order.getId());
        verify(jdbcTemplate).update(query);
    }

    @Test
    void shouldFindById() {
        when(orderAssembly.findAllOrdersSingleLayer()).thenReturn(orderList);
        when(customerAssembly.findAllCustomers()).thenReturn(customerList);

        Optional<Order> actualOptional = testInstance.findById(ORDER_ID);

        assertThat(actualOptional).isPresent();
        Order actual = actualOptional.get();
        assertThat(actual).isEqualTo(order);
    }

    @Test
    void shouldFindOrdersByCustomerId() {
        when(orderAssembly.findAllOrdersSingleLayer()).thenReturn(orderList);
        when(customerAssembly.findAllCustomers()).thenReturn(customerList);

        List<Order> actual = testInstance.findOrdersByCustomerId(CUSTOMER_ID);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual).contains(order);
    }
}
