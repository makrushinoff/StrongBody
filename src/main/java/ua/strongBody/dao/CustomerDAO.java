package ua.strongBody.dao;

import org.springframework.dao.DataAccessException;
import ua.strongBody.models.Customer;

import java.util.Optional;

public interface CustomerDAO extends GeneralDAO<Customer> {

    Optional<Customer> findFirstByUsername(String username);

    void saveWithoutId(Customer customer) throws DataAccessException;
}
