package ua.strongBody.validation;

import org.springframework.stereotype.Service;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.exceptions.FieldValidationException;
import ua.strongBody.exceptions.ValidationException;
import ua.strongBody.models.Customer;
import ua.strongBody.models.forms.RegistrationForm;

import java.util.List;

import static ua.strongBody.models.forms.RegistrationForm.*;

/**
 * CustomerValidator is a class, that should validate input data
 * from {@link RegistrationForm}
 */
@Service
public class RegistrationFormValidator implements Validator<RegistrationForm> {
    private final CustomerDAO customerDAO;

    private static final String VALIDATION_EXCEPTION_PATTERN = "Field: '%s', value: '%s' , reason: '%s'";
    private static final String DUPLICATION_REASON = "Duplicated instance in database";

    public RegistrationFormValidator(CustomerDAOImpl customerDAO) {
        this.customerDAO = customerDAO;
    }

    /**
     * Method validates fields from {@link RegistrationForm}.
     *
     * If some conditions is not passed,
     * method going to throw FieldValidationException {@link FieldValidationException}.
     * And then it wraps it into {@link ValidationException}
     *
     * @param registrationForm - data from customer, that is got from registration process.
     * @see Customer
     */
    public void validate(RegistrationForm registrationForm) throws ValidationException {
        List<Customer> allCustomers = customerDAO.findAll();
        try {
            allCustomers.forEach(customer -> validateFields(registrationForm, customer));
        } catch (FieldValidationException ex) {
            String message = generateValidationMessage(ex);
            throw new ValidationException(message);
        }
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
