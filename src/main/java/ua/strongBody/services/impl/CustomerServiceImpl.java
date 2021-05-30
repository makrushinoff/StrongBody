package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Customer;
import ua.strongBody.services.CustomerService;

import java.util.List;
import java.util.Optional;
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
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, username);
        Optional<Customer> customerOptional = customerDAO.findFirstByUsername(username);
        return processInstanceExport(customerOptional, username, Customer.USERNAME_FIELD);
    }

    @Override
    public List<Customer> findAll() {
        LOG.debug(LOG_DEBUG_EMPTY_PATTERN);
        return customerDAO.findAll();
    }

    @Override
    public void save(Customer customer) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, customer);
        customerDAO.save(customer);
    }

    @Override
    public void updateById(UUID id, Customer customer) {
        LOG.debug(LOG_DEBUG_TWO_ARG_PATTERN, customer, id);
        customerDAO.updateById(id, customer);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        customerDAO.deleteById(id);
    }

    @Override
    public Customer findById(UUID id) {
        LOG.info(LOG_DEBUG_ONE_ARG_PATTERN, id);
        Optional<Customer> customerOptional = customerDAO.findById(id);
        return processInstanceExport(customerOptional, id.toString(), Customer.ID_FIELD);
    }

    private Customer processInstanceExport(Optional<Customer> customerOptional, String requestedValue, String fieldName) {
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        }
        processGeneralCustomerException(fieldName, requestedValue);
        return null;
    }

    private void processGeneralCustomerException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_CUSTOMER_NOT_FOUND_PATTERN, invalidField, invalidValue);
        LOG.warn(message);
        throw new FieldNotFoundException(message);
    }
}
