package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Customer;
import ua.strongBody.services.CustomerService;

import java.util.List;
import java.util.UUID;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Customer findByUsername(String username) throws FieldNotFoundException {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), username);
        return customerDAO.findFirstByUsername(username)
                .orElseThrow(() -> generateGeneralCustomerException(Customer.USERNAME_FIELD, username));
    }

    @Override
    public List<Customer> findAll() {
        LOG.debug(LOG_DEBUG_EMPTY_PATTERN.getMessage());
        return customerDAO.findAll();
    }

    @Override
    public void save(Customer customer) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), customer);
        customerDAO.save(customer);
    }

    @Override
    public void updateById(UUID id, Customer customer) {
        LOG.debug(LOG_DEBUG_TWO_ARG_PATTERN.getMessage(), customer, id);
        customerDAO.updateById(id, customer);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), id);
        customerDAO.deleteById(id);
    }

    @Override
    public Customer findById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), id);
        return customerDAO.findById(id)
                .orElseThrow(() -> generateGeneralCustomerException(Customer.ID_FIELD, id.toString()));
    }

    private RuntimeException generateGeneralCustomerException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_CUSTOMER_NOT_FOUND_PATTERN.getMessage(), invalidField, invalidValue);
        LOG.warn(message);
        return new FieldNotFoundException(message);
    }
}
