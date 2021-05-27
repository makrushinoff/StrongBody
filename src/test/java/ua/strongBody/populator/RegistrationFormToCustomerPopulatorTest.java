package ua.strongBody.populator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Role;
import ua.strongBody.models.State;
import ua.strongBody.models.forms.RegistrationForm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationFormToCustomerPopulatorTest {

    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String EMAIL = "cus@bla.com";
    private static final String PHONE_NUMBER = "+99999999";
    private static final State STATE = State.ACTIVE;
    private static final Role ROLE = Role.USER;

    private RegistrationForm registrationForm;
    private Customer customer;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationFormToCustomerPopulator testInstance;

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setFirstName(FIRST_NAME);
        registrationForm.setLastName(LAST_NAME);
        registrationForm.setUsername(USERNAME);
        registrationForm.setPassword(PASSWORD);
        registrationForm.setEmail(EMAIL);
        registrationForm.setPhoneNumber(PHONE_NUMBER);

        customer = new Customer();

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
    }

    @Test
    void shouldPopulate() {
        testInstance.convert(registrationForm, customer);

        assertThat(customer.getEmail()).isEqualTo(EMAIL);
        assertThat(customer.getUsername()).isEqualTo(USERNAME);
        assertThat(customer.getPassword()).isEqualTo(ENCODED_PASSWORD);
        assertThat(customer.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(customer.getLastName()).isEqualTo(LAST_NAME);
        assertThat(customer.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        assertThat(customer.getState()).isEqualTo(STATE);
        assertThat(customer.getRole()).isEqualTo(ROLE);
    }
}
