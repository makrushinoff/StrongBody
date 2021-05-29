package ua.strongBody.services;

import ua.strongBody.exceptions.UsernameNotFoundException;
import ua.strongBody.models.Customer;

import java.util.Optional;

public interface CustomerService extends GeneralService<Customer> {

    Customer findByUsername(String username) throws UsernameNotFoundException;

}
