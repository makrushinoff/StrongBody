package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Role;
import ua.strongBody.models.State;
import ua.strongBody.populator.CustomerToCustomerDetailsPopulator;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerDetailsServiceImplTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final State ACTIVE_STATE = State.ACTIVE;
    private static final Role USER_ROLE = Role.USER;

    private Customer customer;

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private CustomerToCustomerDetailsPopulator customerToCustomerDetailsPopulator;

    @InjectMocks
    private CustomerDetailsServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(ID);
        customer.setUsername(USERNAME);
        customer.setEmail(EMAIL);
        customer.setPassword(PASSWORD);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setPhoneNumber(PHONE_NUMBER);
        customer.setState(ACTIVE_STATE);
        customer.setRole(USER_ROLE);

        Optional<Customer> customerOptional = Optional.of(customer);

        when(customerDAO.findFirstByUsername(USERNAME)).thenReturn(customerOptional);
    }

    @Test
    void shouldLoadUser() {
        testInstance.loadUserByUsername(USERNAME);

        verify(customerToCustomerDetailsPopulator).convert(eq(customer), any());
    }

    @Test
    void shouldThrowAnException() {
        when(customerDAO.findFirstByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> testInstance.loadUserByUsername(USERNAME));
    }
}
