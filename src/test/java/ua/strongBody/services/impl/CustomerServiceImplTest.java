package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Role;
import ua.strongBody.models.State;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private static final String EMAIL = "cus@bla.com";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String PHONE_NUMBER = "+99999999";

    private Customer customer;
    private Customer customer2;
    private Optional<Customer> customerOptional;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private CustomerServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setEmail(EMAIL);
        customer.setUsername(USERNAME);
        customer.setPassword(PASSWORD);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setPhoneNumber(PHONE_NUMBER);
        customer.setRole(Role.USER);
        customer.setState(State.ACTIVE);
        customer.setId(UUID.randomUUID());

        customer2 = new Customer();
        customer2.setEmail(EMAIL);
        customer2.setUsername(USERNAME);
        customer2.setPassword(PASSWORD);
        customer2.setFirstName(FIRST_NAME);
        customer2.setLastName(LAST_NAME);
        customer2.setPhoneNumber(PHONE_NUMBER);
        customer2.setRole(Role.USER);
        customer2.setState(State.ACTIVE);
        customer2.setId(UUID.randomUUID());

        customerOptional = Optional.of(customer);
    }

    @Test
    void shouldFindByUsername() {
        when(customerDAO.findFirstByUsername(USERNAME)).thenReturn(customerOptional);

        Optional<Customer> actualOptional = testInstance.findByUsername(USERNAME);

        assertThat(actualOptional).isPresent();
        Customer actual = actualOptional.get();
        assertThat(actual.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void shouldFindAll() {
        when(customerDAO.findAll()).thenReturn(Collections.singletonList(customer));

        List<Customer> result = testInstance.findAll();

        assertThat(result).isEqualTo(Collections.singletonList(customer));
    }

    @Test
    void shouldSave() {
        testInstance.save(customer);

        verify(customerDAO).save(customer);
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(customer.getId(), customer);

        verify(customerDAO).updateById(customer.getId(), customer);
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(customer.getId());

        verify(customerDAO).deleteById(customer.getId());
    }

    @Test
    void shouldFindById() {
        when(customerDAO.findById(customer.getId())).thenReturn(customerOptional);

        Optional<Customer> result = testInstance.findById(customer.getId());

        assertThat(result.get()).isEqualTo(customerOptional.get());
    }
}
