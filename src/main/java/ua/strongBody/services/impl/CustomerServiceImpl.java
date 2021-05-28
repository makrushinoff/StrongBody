package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.models.Customer;
import ua.strongBody.services.CustomerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private static final String LOG_INFO_EMPTY_PATTERN = "Method was called.";

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        LOG.info(String.format("Method was called with argument(s): '%s'.", username));
        return customerDAO.findFirstByUsername(username);
    }

    @Override
    public List<Customer> findAll() {
        LOG.info(LOG_INFO_EMPTY_PATTERN);
        return customerDAO.findAll();
    }

    @Override
    public void save(Customer customer) {
        LOG.info(String.format("Method was called with argument(s): '%s'.", customer));
        customerDAO.save(customer);
    }

    @Override
    public void updateById(UUID id, Customer customer) {
        String logMessage = "Method was called with argument(s): Customer: '%s', id: '%s'.";
        LOG.info(String.format(logMessage, customer, id));
        customerDAO.updateById(id, customer);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.info(String.format("Method was called with argument(s): '%s'.", id));
        customerDAO.deleteById(id);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        LOG.info(String.format("Method was called with argument(s): '%s'.", id));
        return customerDAO.findById(id);
    }
}
