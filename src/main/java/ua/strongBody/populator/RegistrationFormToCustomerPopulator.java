package ua.strongBody.populator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Role;
import ua.strongBody.models.State;
import ua.strongBody.models.forms.RegistrationForm;

@Component
public class RegistrationFormToCustomerPopulator implements Populator<RegistrationForm, Customer> {

    private static final State DEFAULT_STATE = State.ACTIVE;
    private static final Role DEFAULT_ROLE = Role.USER;

    private final PasswordEncoder passwordEncoder;

    public RegistrationFormToCustomerPopulator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void populate(RegistrationForm registrationForm, Customer customer) {
        customer.setEmail(registrationForm.getEmail());
        customer.setUsername(registrationForm.getUsername());
        populatePassword(registrationForm, customer);
        customer.setFirstName(registrationForm.getFirstName());
        customer.setLastName(registrationForm.getLastName());
        customer.setPhoneNumber(registrationForm.getPhoneNumber());
        customer.setState(DEFAULT_STATE);
        customer.setRole(DEFAULT_ROLE);
    }

    private void populatePassword(RegistrationForm registrationForm, Customer customer) {
        String password = registrationForm.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        customer.setPassword(encodedPassword);
    }
}
