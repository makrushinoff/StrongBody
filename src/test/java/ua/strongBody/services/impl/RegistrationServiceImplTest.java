package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.models.Customer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    private RegistrationForm registrationForm;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerDAOImpl customerDAOImpl;

    @InjectMocks
    private RegistrationServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
    }

    @Test
    void shouldRegistration() {
        testInstance.registration(registrationForm);

        verify(customerDAOImpl).save(any(Customer.class));
    }
}
