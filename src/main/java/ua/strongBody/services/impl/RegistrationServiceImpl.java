package ua.strongBody.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Role;
import ua.strongBody.models.State;
import ua.strongBody.services.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerDAO customerDAO;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(CustomerDAOImpl customerDAO, PasswordEncoder passwordEncoder) {
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
