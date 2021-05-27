package ua.strongBody.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.models.Customer;
import ua.strongBody.models.CustomerDetails;
import ua.strongBody.populator.Populator;

import java.util.Optional;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private static final String USER_NOT_FOUND_EXCEPTION_PATTERN = "user with username '%s' not found";

    private final CustomerDAO customerDAO;
    private final Populator<Customer, CustomerDetails> populator;

    public CustomerDetailsServiceImpl(CustomerDAO customerDAO, Populator<Customer, CustomerDetails> populator) {
        this.customerDAO = customerDAO;
        this.populator = populator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerDAO.findFirstByUsername(username);
        if (customerOptional.isEmpty()) {
            processException(username);
        }

        Customer customer = customerOptional.get();
        return processCustomerDetails(customer);
    }

    private CustomerDetails processCustomerDetails(Customer customer) {
        CustomerDetails customerDetails = new CustomerDetails();
        populator.convert(customer, customerDetails);
        return customerDetails;
    }

    private void processException(String username) {
        String exceptionMessage = String.format(USER_NOT_FOUND_EXCEPTION_PATTERN, username);
        throw new UsernameNotFoundException(exceptionMessage);
    }
}
