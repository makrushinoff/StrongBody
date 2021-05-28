package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.BadSqlGrammarException;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.exceptions.ValidationException;
import ua.strongBody.models.Customer;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.populator.RegistrationFormToCustomerPopulator;
import ua.strongBody.validation.RegistrationFormValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    private RegistrationForm registrationForm;

    @Mock
    private RegistrationFormValidator registrationFormValidator;

    @Mock
    private RegistrationFormToCustomerPopulator registrationFormToCustomerPopulator;

    @Mock
    private CustomerDAOImpl customerDAOImpl;

    @InjectMocks
    private RegistrationServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
    }

    @Test
    void shouldRegister() throws ValidationException {
        boolean actual = testInstance.register(registrationForm);

        assertThat(actual).isTrue();
        verify(registrationFormValidator).validate(registrationForm);
        verify(registrationFormToCustomerPopulator).convert(eq(registrationForm), any(Customer.class));
        verify(customerDAOImpl).saveWithoutId(any(Customer.class));
    }

    @Test
    void shouldNotRegisterInvalidForm() throws ValidationException {
        doThrow(ValidationException.class).when(registrationFormValidator).validate(registrationForm);

        boolean actual = testInstance.register(registrationForm);

        assertThat(actual).isFalse();
        verify(registrationFormToCustomerPopulator, never()).convert(eq(registrationForm), any(Customer.class));
        verify(customerDAOImpl, never()).save(any(Customer.class));
    }

    @Test
    void shouldNotRegisterOnSaveFail() throws ValidationException {
        doThrow(BadSqlGrammarException.class).when(customerDAOImpl).saveWithoutId(any(Customer.class));

        boolean actual = testInstance.register(registrationForm);

        assertThat(actual).isFalse();
        verify(registrationFormValidator).validate(registrationForm);
        verify(registrationFormToCustomerPopulator).convert(eq(registrationForm), any(Customer.class));
    }
}
