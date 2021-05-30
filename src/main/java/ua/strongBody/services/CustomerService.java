package ua.strongBody.services;

import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Customer;

public interface CustomerService extends GeneralService<Customer> {

    Customer findByUsername(String username) throws FieldNotFoundException;

}
