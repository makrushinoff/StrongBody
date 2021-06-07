package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CartDAO;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.exceptions.ValidationException;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.populator.Populator;
import ua.strongBody.populator.RegistrationFormToCustomerPopulator;
import ua.strongBody.services.RegistrationService;
import ua.strongBody.validation.RegistrationFormValidator;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final RegistrationFormValidator registrationFormValidator;
    private final Populator<RegistrationForm, Customer> populator;
    private final CustomerDAO customerDAO;
    private final CartDAO cartDAO;

    public RegistrationServiceImpl(RegistrationFormValidator registrationFormValidator, CustomerDAOImpl customerDAO, RegistrationFormToCustomerPopulator populator, CartDAO cartDAO) {
        this.registrationFormValidator = registrationFormValidator;
        this.customerDAO = customerDAO;
        this.populator = populator;
        this.cartDAO = cartDAO;
    }

    @Override
    public boolean register(RegistrationForm registrationForm) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), registrationForm);
        if (!isRegistrationFormValid(registrationForm)) {
            return false;
        }

        Customer customer = convertRegistrationFormToCustomer(registrationForm);

        if (!isCustomerSavedSuccessfully(customer)) {
            return false;
        }

        processSuccessMessage(customer);
        return true;
    }

    private void processSuccessMessage(Customer customer) {
        String username = customer.getUsername();
        String message = String.format(REGISTRATION_SUCCESS_PATTERN.getMessage(), username);
        LOG.info(message);
    }

    private boolean isCustomerSavedSuccessfully(Customer customer) {
        try {
            processCustomerCreation(customer);
        } catch (DataAccessException ex) {
            String message = String.format(SAVE_FAILED_PATTERN.getMessage(), ex.getMessage());
            LOG.warn(message);
            return false;
        }
        return true;
    }

    private void processCustomerCreation(Customer customer) {
        customerDAO.save(customer);
        Cart cart = createCartForNewCustomer(customer);
        cartDAO.saveWithoutId(cart);
    }

    private Cart createCartForNewCustomer(Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer);
        return cart;
    }

    private boolean isRegistrationFormValid(RegistrationForm registrationForm) {
        try {
            registrationFormValidator.validate(registrationForm);
        } catch (ValidationException ex) {
            String message = String.format(VALIDATION_FAILED_PATTERN.getMessage(), ex.getMessage());
            LOG.warn(message);
            return false;
        }
        return true;
    }

    private Customer convertRegistrationFormToCustomer(RegistrationForm registrationForm) {
        Customer customer = new Customer();
        populator.convert(registrationForm, customer);
        return customer;
    }
}
