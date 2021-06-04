package ua.strongBody.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.strongBody.assembly.CustomerAssembly;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.models.Customer;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerDAOImplUnitTest {

    private static final String SAVE_QUERY = "INSERT INTO customer (id, email, username, password, first_name, last_name, phone_number)" +
            " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String SAVE_WITHOUT_ID_QUERY = "INSERT INTO customer (email, username, password, first_name, last_name, phone_number)" +
            " VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String UPDATE_BY_ID_QUERY = "UPDATE customer SET email = '%s', username = '%s', password = '%s', first_name = '%s', last_name = '%s', phone_number = '%s' WHERE id = '%s'";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM customer WHERE id = '%s'";

    private static final UUID ID = UUID.randomUUID();
    private static final String EMAIL = "cus@bla.com";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String PHONE_NUMBER = "+99999999";

    private Customer customer;
    private List<Customer> customerList;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private CustomerAssembly customerAssembly;

    @InjectMocks
    private CustomerDAOImpl testInstance;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(ID);
        customer.setEmail(EMAIL);
        customer.setUsername(USERNAME);
        customer.setPassword(PASSWORD);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setPhoneNumber(PHONE_NUMBER);

        customerList = Collections.singletonList(customer);
    }

    @Test
    void shouldSave() {
        testInstance.save(customer);

        verify(jdbcTemplate).update(String.format(SAVE_QUERY, ID, EMAIL, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE_NUMBER));
    }

    @Test
    void shouldSaveWithoutId() {
        testInstance.saveWithoutId(customer);

        verify(jdbcTemplate).update(String.format(SAVE_WITHOUT_ID_QUERY, EMAIL, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE_NUMBER));
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ID, customer);

        verify(jdbcTemplate).update(String.format(UPDATE_BY_ID_QUERY, EMAIL, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, PHONE_NUMBER, ID));
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ID);

        verify(jdbcTemplate).update(String.format(DELETE_BY_ID_QUERY, ID));
    }

    @Test
    void shouldFindById() {
        when(customerAssembly.findAllCustomers()).thenReturn(customerList);

        Optional<Customer> actualOptional = testInstance.findById(ID);
        Customer actual = actualOptional.get();

        assertThat(customerList.contains(actual)).isTrue();
        assertThat(customerList.get(0)).isEqualTo(actual);
    }

    @Test
    void shouldFindFirstByUsername() {
        when(customerAssembly.findAllCustomers()).thenReturn(customerList);

        Optional<Customer> actualOptional = testInstance.findFirstByUsername(USERNAME);
        Customer actual = actualOptional.get();

        assertThat(customerList.contains(actual)).isTrue();
        assertThat(customerList.get(0)).isEqualTo(actual);
    }
}
