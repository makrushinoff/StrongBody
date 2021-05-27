package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.exceptions.ValidationException;
import ua.strongBody.models.Customer;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.populator.Populator;
import ua.strongBody.populator.RegistrationFormToCustomerPopulator;
import ua.strongBody.services.RegistrationService;
import ua.strongBody.validation.RegistrationFormValidator;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private static final String VALIDATION_FAILED_PATTERN = "Validation stage is failed. Message: '%s'";
    private static final String SAVE_FAILED_MESSAGE = "User save process is failed. Exception: '%s'";

    private final RegistrationFormValidator registrationFormValidator;
    private final Populator<RegistrationForm, Customer> populator;
    private final CustomerDAO customerDAO;

    public RegistrationServiceImpl(RegistrationFormValidator registrationFormValidator, CustomerDAOImpl customerDAO, RegistrationFormToCustomerPopulator populator) {
        this.registrationFormValidator = registrationFormValidator;
        this.customerDAO = customerDAO;
        this.populator = populator;
    }

    @Override
    public boolean register(RegistrationForm registrationForm) {
        if (!isRegistrationFormValid(registrationForm)) {
            return false;
        }
        Customer customer = convertRegistrationFormToCustomer(registrationForm);
        return isCustomerSavedSuccessfully(customer);
    }

    private boolean isCustomerSavedSuccessfully(Customer customer) {
        try {
            customerDAO.saveWithoutId(customer);
        } catch (DataAccessException ex) {
            String message = String.format(SAVE_FAILED_MESSAGE, ex.getMessage());
            LOG.warn(message);
            return false;
        }
        return true;
    }

    private boolean isRegistrationFormValid(RegistrationForm registrationForm) {
        try {
            registrationFormValidator.validate(registrationForm);
        } catch (ValidationException ex) {
            String message = String.format(VALIDATION_FAILED_PATTERN, ex.getMessage());
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
