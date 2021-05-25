package ua.com.sportfood.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.sportfood.dao.CustomerDAO;
import ua.com.sportfood.models.Customer;
import ua.com.sportfood.models.Role;
import ua.com.sportfood.models.State;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;

    public CustomerDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = jdbcTemplate.query("SELECT * FROM customer", (resultSet, i) -> parseResultSetToCustomer(resultSet));
        return customers;
    }

    @Override
    public void save(Customer customer) {
        jdbcTemplate.update("INSERT INTO customer VALUES(?, ?, ?, ?, ?, ?, ? , ? , ?)",
                customer.getId(),
                customer.getEmail(),
                customer.getUsername(),
                customer.getPassword(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getState().toString(),
                customer.getRole().toString()


        );
    }

    @Override
    public void updateById(UUID id, Customer customer) {
        jdbcTemplate.update("UPDATE customer SET " +
                        "email = ?, " +
                        "username = ?, " +
                        "password = ?, " +
                        "first_name = ?, " +
                        "last_name = ?, " +
                        "phone_number = ? ",
                customer.getEmail(),
                customer.getUsername(),
                customer.getPassword(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber());
    }

    @Override
    public void deleteById(UUID customerId) {
        jdbcTemplate.update("DELETE FROM customer WHERE id = '" + customerId + "'");
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        List<Customer> customerList = findAll();
        List<Customer> filteredList = customerList.stream()
                .filter(customer -> customer.getId().equals(id)).collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            return Optional.empty();
        }

        Customer result = filteredList.get(0);
        return Optional.of(result);
    }

    @Override
    public Optional<Customer> findFirstByUsername(String username) {
        List<Customer> customerList = findAll();
        List<Customer> filteredList = customerList.stream()
                .filter(customer -> customer.getUsername().equals(username))
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            return Optional.empty();
        }

        Customer result = filteredList.get(0);
        return Optional.of(result);
    }

    private Customer parseResultSetToCustomer(ResultSet resultSetCustomer) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSetCustomer.getObject("id", UUID.class));
        customer.setEmail(resultSetCustomer.getString("email"));
        customer.setFirstName(resultSetCustomer.getString("first_name"));
        customer.setLastName(resultSetCustomer.getString("last_name"));
        customer.setPassword(resultSetCustomer.getString("password"));
        customer.setUsername(resultSetCustomer.getString("username"));
        customer.setPhoneNumber(resultSetCustomer.getString("phone_number"));
        String state = resultSetCustomer.getString("customer_state");
        if (state.equals(State.ACTIVE.toString())) {
            customer.setState(State.ACTIVE);
        } else if (state.equals(State.BANNED.toString())) {
            customer.setState(State.BANNED);
        } else {
            customer.setState(State.DELETED);
        }
        String role = resultSetCustomer.getString("customer_role");
        if (role.equals(Role.ADMIN.toString())) {
            customer.setRole(Role.ADMIN);
        } else {
            customer.setRole(Role.USER);
        }
        return customer;
    }
}


