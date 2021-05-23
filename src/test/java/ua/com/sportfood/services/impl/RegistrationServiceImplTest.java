package ua.com.sportfood.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.sportfood.dao.CustomerDAO;
import ua.com.sportfood.models.RegistrationForm;
import ua.com.sportfood.models.Customer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    private RegistrationForm registrationForm;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private RegistrationServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
    }

    @Test
    void shouldRegistration() {
        testInstance.registration(registrationForm);

        verify(customerDAO).save(any(Customer.class));
    }
}
