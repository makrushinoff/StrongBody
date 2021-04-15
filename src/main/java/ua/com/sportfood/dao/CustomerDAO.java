package ua.com.sportfood.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.sportfood.models.Customer;

import java.util.List;
import java.util.UUID;

@Repository
public class CustomerDAO implements GeneralDAO<Customer>{
    private JdbcTemplate jdbcTemplate;

    public CustomerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = jdbcTemplate.query("SELECT * FROM customer", new BeanPropertyRowMapper<>(Customer.class));
        return customers;
    }

    @Override
    public void save(Customer customer) {
        jdbcTemplate.update("INSERT INTO authorization_data VALUES(?, ?, ?, ?, ?, ?, ?)",
                customer.getAuthorizationData().getId(),
                customer.getAuthorizationData().getEmail(),
                customer.getAuthorizationData().getUsername(),
                customer.getAuthorizationData().getPassword(),
                customer.getAuthorizationData().getFirstName(),
                customer.getAuthorizationData().getLastName(),
                customer.getAuthorizationData().getPhoneNumber());
        jdbcTemplate.update("INSERT INTO customer VALUES(?, ?)",
                customer.getId(),
                customer.getAuthorizationData().getId());
    }

    @Override
    public void updateById(UUID id, Customer customer) {
        jdbcTemplate.update("UPDATE authorization_data SET " +
                "email = ?, " +
                "username = ?, " +
                "password = ?, " +
                "first_name = ?, " +
                "last_name = ?, " +
                "phone_number = ? " +
                "WHERE (SELECT authorization_id FROM customer WHERE id = '" + id + "')",
                customer.getAuthorizationData().getEmail(),
                customer.getAuthorizationData().getUsername(),
                customer.getAuthorizationData().getPassword(),
                customer.getAuthorizationData().getFirstName(),
                customer.getAuthorizationData().getLastName(),
                customer.getAuthorizationData().getPhoneNumber());
    }

    @Override
    public void deleteById(UUID customerId) {
        jdbcTemplate.update("DELETE  * FROM authorization_data WHERE (" +
                "SELECT authorization_id FROM customer WHERE id = '" + customerId + "')");
        jdbcTemplate.update("DELETE * FROM customer WHERE id = '" + customerId + "'");
    }

    @Override
    public Customer findById(UUID id) {
        Customer customer = jdbcTemplate.query("SELECT * FROM customer WHERE id = '" + id + "'",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Customer.class)).stream().
                findAny().orElse(new Customer());
        return customer;
    }

    public Customer findByUsername(String username) {
        Customer customer = jdbcTemplate.query("SELECT * FROM authorization_data WHERE username = '" + username + "'",
                new Object[]{username},
                new BeanPropertyRowMapper<>(Customer.class)).stream().
                findAny().orElse(new Customer());
        return customer;
    }
}
