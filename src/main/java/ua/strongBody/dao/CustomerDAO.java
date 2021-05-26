package ua.strongBody.dao;

import ua.strongBody.models.Customer;

import java.util.Optional;

public interface CustomerDAO extends GeneralDAO<Customer> {
    Optional<Customer> findFirstByUsername(String username);

    void saveWithId(Customer customer);
}
