package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.exceptions.IdNotFoundException;
import ua.strongBody.exceptions.UsernameNotFoundException;
import ua.strongBody.models.Customer;
import ua.strongBody.services.CustomerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private static final String LOG_INFO_EMPTY_PATTERN = "Method was called.";
    private static final String LOG_INFO_ONE_ARG_PATTERN = "Method was called with argument: '{}'.";
    private static final String LOG_INFO_TWO_ARG_PATTERN = "Method was called with argument(s): Customer: '{}', id: '{}'.";

    private static final String GENERAL_CUSTOMER_NOT_FOUND_PATTERN = "Customer with %s: '%s' not found!";

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Customer findByUsername(String username) throws UsernameNotFoundException {
        LOG.info(LOG_INFO_ONE_ARG_PATTERN, username);
        Optional<Customer> customerOptional = customerDAO.findFirstByUsername(username);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        }
        processUsernameNotFoundException(username);
        return null;
    }

    private void processUsernameNotFoundException(String username) throws UsernameNotFoundException {
        String message = String.format(GENERAL_CUSTOMER_NOT_FOUND_PATTERN, Customer.USERNAME_FIELD, username);
        LOG.warn(message);
        throw new UsernameNotFoundException(message);
    }

    private void processIdNotFoundException(UUID id) {
        String message = String.format(GENERAL_CUSTOMER_NOT_FOUND_PATTERN, Customer.ID_FIELD, id);
        LOG.warn(message);
        throw new IdNotFoundException(message);
    }

    @Override
    public List<Customer> findAll() {
        LOG.info(LOG_INFO_EMPTY_PATTERN);
        return customerDAO.findAll();
    }

    @Override
    public void save(Customer customer) {
        LOG.info(LOG_INFO_ONE_ARG_PATTERN, customer);
        customerDAO.save(customer);
    }

    @Override
    public void updateById(UUID id, Customer customer) {
        LOG.info(LOG_INFO_TWO_ARG_PATTERN, customer, id);
        customerDAO.updateById(id, customer);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.info(LOG_INFO_ONE_ARG_PATTERN, id);
        customerDAO.deleteById(id);
    }

    @Override
    public Customer findById(UUID id) {
        LOG.info(LOG_INFO_ONE_ARG_PATTERN, id);
        Optional<Customer> customerOptional = customerDAO.findById(id);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        }
        processIdNotFoundException(id);
        return null;
    }
}
