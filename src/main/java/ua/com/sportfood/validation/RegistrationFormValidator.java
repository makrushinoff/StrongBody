package ua.com.sportfood.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.com.sportfood.dao.CustomerDAO;
import ua.com.sportfood.exceptions.FieldValidationException;
import ua.com.sportfood.exceptions.ValidationException;
import ua.com.sportfood.models.forms.RegistrationForm;
import ua.com.sportfood.models.Customer;

import java.util.List;

import static ua.com.sportfood.models.forms.RegistrationForm.*;

/**
 * CustomerValidator is a class, that should validate input data
 * from {@link RegistrationForm}
 */
@Service
public class RegistrationFormValidator implements Validator<Customer, RegistrationForm> {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationFormValidator.class);

    private final CustomerDAO customerDAO;

    private static final String VALIDATION_EXCEPTION_PATTERN = "VALIDATION FAIL. Field: '%s', value: '%s' , reason: '%s'";
    private static final String DUPLICATION_REASON = "Duplicated instance in database";

    public RegistrationFormValidator(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    /**
     * Method check: Customer data from {@link RegistrationForm} is present
     * in database or not.
     * If present - throws {@link FieldValidationException} - custom Exception,
     * that was made for identifying this problem
     * If does not - returns empty {@link Customer} object, that will be registered in database,
     * due to his/her registration form.
     *
     * @param form - data from customer, that is got from registration form
     * @return new Customer object.
     * @see Customer
     */
    public Customer validate(RegistrationForm form) throws ValidationException {
        List<Customer> allCustomers = customerDAO.findAll();
        try {
            allCustomers.forEach(customer -> validateFields(form, customer));
        } catch (FieldValidationException ex) {
            String message = generateValidationMessage(ex);
            LOG.warn(message);
            throw new ValidationException(message);
        }
        return new Customer();
    }

    private String generateValidationMessage(FieldValidationException ex) {
        String invalidFieldName = ex.getInvalidFieldName();
        String invalidValue = ex.getInvalidValue();
        String reason = ex.getReason();
        return String.format(VALIDATION_EXCEPTION_PATTERN, invalidFieldName, invalidValue, reason);
    }

    private void validateFields(RegistrationForm form, Customer customer) throws FieldValidationException {
        usernameValidation(form, customer);
        emailValidation(form, customer);
        phoneNumberValidation(form, customer);
    }

    private void emailValidation(RegistrationForm form, Customer customer) {
        boolean emailCheck = customer.getEmail().equals(form.getEmail());
        if (emailCheck) {
            throw new FieldValidationException(EMAIL_FIELD, form.getEmail(), DUPLICATION_REASON);
        }
    }

    private void phoneNumberValidation(RegistrationForm form, Customer customer) {
        boolean phoneNumberCheck = customer.getPhoneNumber().equals(form.getPhoneNumber());
        if (phoneNumberCheck) {
            throw new FieldValidationException(PHONE_NUMBER_FIELD, form.getPhoneNumber(), DUPLICATION_REASON);
        }
    }

    private void usernameValidation(RegistrationForm form, Customer customer) throws FieldValidationException {
        boolean usernameCheck = customer.getUsername().equals(form.getUsername());
        if (usernameCheck) {
            throw new FieldValidationException(USERNAME_FIELD, form.getUsername(), DUPLICATION_REASON);
        }
    }
}
