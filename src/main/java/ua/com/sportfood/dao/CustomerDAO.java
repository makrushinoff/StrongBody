package ua.com.sportfood.dao;

import ua.com.sportfood.models.Customer;

import java.util.Optional;

public interface CustomerDAO extends GeneralDAO<Customer> {
    Optional<Customer> findFirstByUsername(String username);
}
