package ua.com.sportfood.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.sportfood.dao.CustomerDAO;
import ua.com.sportfood.formRegistration.RegistrationForm;
import ua.com.sportfood.models.Customer;
import ua.com.sportfood.models.Role;
import ua.com.sportfood.models.State;
import ua.com.sportfood.services.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerDAO customerDAO;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(CustomerDAO customerDAO, PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registration(RegistrationForm userForm) {
        String password = passwordEncoder.encode(userForm.getPassword());
        Customer customer = new Customer();
        customer.setEmail(userForm.getEmail());
        customer.setFirstName(userForm.getFirstName());
        customer.setLastName(userForm.getLastName());
        customer.setUsername(customer.getUsername());
        customer.setPassword(password);
        customer.setPhoneNumber(userForm.getPhoneNumber());
        customer.setRole(Role.USER);
        customer.setState(State.ACTIVE);

        customerDAO.save(customer);
    }
}
