package ua.com.sportfood.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.sportfood.dao.CustomerDAO;
import ua.com.sportfood.exceptions.ValidationException;
import ua.com.sportfood.models.forms.RegistrationForm;
import ua.com.sportfood.models.Customer;
import ua.com.sportfood.validation.RegistrationFormValidator;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationFormValidatorTest {

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private RegistrationFormValidator testInstance;

    private final static String FIRST_NAME = "firstName";
    private final static String LAST_NAME = "lastLast";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "email";
    private final static String EMAIL2 = "email2";
    private final static String USERNAME = "username";
    private final static String USERNAME2 = "username2";
    private final static String PHONE_NUMBER = "phoneNumber";
    private final static String PHONE_NUMBER2 = "phoneNumber2";
    private final static Customer EMPTY_CUSTOMER = new Customer();

    private RegistrationForm registrationForm;
    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setUsername(USERNAME);
        customer.setLastName(LAST_NAME);
        customer.setFirstName(FIRST_NAME);
        customer.setEmail(EMAIL);
        customer.setPhoneNumber(PHONE_NUMBER);
        customer.setPassword(PASSWORD);

        customers = Collections.singletonList(customer);

        registrationForm = new RegistrationForm();
        registrationForm.setUsername(USERNAME);
        registrationForm.setPassword(PASSWORD);
        registrationForm.setFirstName(FIRST_NAME);
        registrationForm.setLastName(LAST_NAME);
        registrationForm.setEmail(EMAIL);
        registrationForm.setPhoneNumber(PHONE_NUMBER);
    }

    @Test
    void shouldCheckCustomerIsPresentByUsername() {
        registrationForm.setEmail(EMAIL2);
        registrationForm.setPhoneNumber(PHONE_NUMBER2);
        when(customerDAO.findAll()).thenReturn(customers);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(registrationForm));
    }

    @Test
    void shouldCheckCustomerIsPresentByEmail() {
        registrationForm.setUsername(USERNAME2);
        registrationForm.setPhoneNumber(PHONE_NUMBER2);
        when(customerDAO.findAll()).thenReturn(customers);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(registrationForm));
    }

    @Test
    void shouldCheckCustomerIsPresentByPhoneNumber() {
        registrationForm.setEmail(EMAIL2);
        registrationForm.setUsername(USERNAME2);
        when(customerDAO.findAll()).thenReturn(customers);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(registrationForm));
    }

    @Test
    void shouldCheckCustomerNotPresent() throws ValidationException {
        registrationForm.setUsername(USERNAME2);
        registrationForm.setEmail(EMAIL2);
        registrationForm.setPhoneNumber(PHONE_NUMBER2);
        when(customerDAO.findAll()).thenReturn(customers);

        Customer actual = testInstance.validate(registrationForm);

        assertThat(actual).isEqualTo(EMPTY_CUSTOMER);
    }
}
