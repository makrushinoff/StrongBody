package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.models.Customer;
import ua.strongBody.models.CustomerDetails;
import ua.strongBody.populator.Populator;

import java.util.Optional;

import static ua.strongBody.constants.LoggingConstants.GENERAL_CUSTOMER_NOT_FOUND_PATTERN;
import static ua.strongBody.constants.LoggingConstants.LOG_DEBUG_ONE_ARG_PATTERN;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDetailsServiceImpl.class);

    private final CustomerDAO customerDAO;
    private final Populator<Customer, CustomerDetails> populator;

    public CustomerDetailsServiceImpl(CustomerDAO customerDAO, Populator<Customer, CustomerDetails> populator) {
        this.customerDAO = customerDAO;
        this.populator = populator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, username);
        Optional<Customer> customerOptional = customerDAO.findFirstByUsername(username);
        Customer customer = customerOptional.orElseThrow(() -> processException(username));
        return processCustomerDetails(customer);
    }

    private CustomerDetails processCustomerDetails(Customer customer) {
        CustomerDetails customerDetails = new CustomerDetails();
        populator.convert(customer, customerDetails);
        return customerDetails;
    }

    private RuntimeException processException(String username) {
        String exceptionMessage = String.format(GENERAL_CUSTOMER_NOT_FOUND_PATTERN, Customer.USERNAME_FIELD, username);
        LOG.warn(exceptionMessage);
        return new UsernameNotFoundException(exceptionMessage);
    }
}
