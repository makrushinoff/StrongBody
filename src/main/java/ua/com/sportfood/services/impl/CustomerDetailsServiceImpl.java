package ua.com.sportfood.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.sportfood.dao.CustomerDAO;
import ua.com.sportfood.models.Customer;
import ua.com.sportfood.populator.CustomerToCustomerDetailsPopulator;
import ua.com.sportfood.models.CustomerDetails;

import java.util.Optional;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private static final String USER_NOT_FOUND_EXCEPTION_PATTERN = "user with username '%s' not found";

    private final CustomerDAO customerDAO;
    private final CustomerToCustomerDetailsPopulator customerToCustomerDetailsPopulator;

    public CustomerDetailsServiceImpl(CustomerDAO customerDAO, CustomerToCustomerDetailsPopulator customerToCustomerDetailsPopulator) {
        this.customerDAO = customerDAO;
        this.customerToCustomerDetailsPopulator = customerToCustomerDetailsPopulator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerDAO.findFirstByUsername(username);
        if (customerOptional.isEmpty()) {
            processException(username);
        }
        var customer = customerOptional.get();

        return processCustomerDetails(customer);
    }

    private CustomerDetails processCustomerDetails(Customer customer) {
        var customerDetails = new CustomerDetails();
        customerToCustomerDetailsPopulator.populate(customer, customerDetails);
        return customerDetails;
    }

    private void processException(String username) {
        var exceptionMessage = String.format(USER_NOT_FOUND_EXCEPTION_PATTERN, username);
        throw new UsernameNotFoundException(exceptionMessage);
    }
}
