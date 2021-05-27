package ua.strongBody.services;

import ua.strongBody.models.Customer;

import java.util.Optional;

public interface CustomerService extends GeneralService<Customer> {

    Optional<Customer> findByUsername(String username);

}
