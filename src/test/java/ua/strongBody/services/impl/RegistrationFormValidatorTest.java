package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.exceptions.ValidationException;
import ua.strongBody.models.Customer;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.validation.RegistrationFormValidator;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationFormValidatorTest {

    @Mock
    private CustomerDAOImpl customerDAOImpl;

    @InjectMocks
    private RegistrationFormValidator testInstance;

    private final static String EMAIL = "email";
    private final static String EMAIL2 = "email2";
    private final static String USERNAME = "username";
    private final static String USERNAME2 = "username2";
    private final static String PHONE_NUMBER = "phoneNumber";
    private final static String PHONE_NUMBER2 = "phoneNumber2";

    private RegistrationForm registrationForm;
    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setUsername(USERNAME);
        customer.setEmail(EMAIL);
        customer.setPhoneNumber(PHONE_NUMBER);

        customers = Collections.singletonList(customer);

        registrationForm = new RegistrationForm();
        registrationForm.setUsername(USERNAME2);
        registrationForm.setEmail(EMAIL2);
        registrationForm.setPhoneNumber(PHONE_NUMBER2);
    }

    @Test
    void shouldValidate() throws ValidationException {
        when(customerDAOImpl.findAll()).thenReturn(customers);

        testInstance.validate(registrationForm);
    }

    @Test
    void shouldThrowExceptionOnSameUsername() {
        registrationForm.setUsername(USERNAME);
        when(customerDAOImpl.findAll()).thenReturn(customers);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(registrationForm));
    }

    @Test
    void shouldThrowExceptionOnSameEmail() {
        registrationForm.setEmail(EMAIL);
        when(customerDAOImpl.findAll()).thenReturn(customers);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(registrationForm));
    }

    @Test
    void shouldThrowExceptionOnSamePhoneNumber() {
        registrationForm.setPhoneNumber(PHONE_NUMBER);
        when(customerDAOImpl.findAll()).thenReturn(customers);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(registrationForm));
    }
}
