package ua.com.sportfood.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.sportfood.models.AuthorizationData;
import ua.com.sportfood.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CustomerDAO implements GeneralDAO<Customer>{
    private JdbcTemplate jdbcTemplate;

    public CustomerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> findAll() {
        List<AuthorizationData> authorizationDataList = jdbcTemplate.query("SELECT * FROM authorization_data", (resultSet, i) -> parseAuthorizationDataFromResultSet(resultSet));
        List<Customer> customers = jdbcTemplate.query("SELECT * FROM customer", (resultSet, i) -> parseResultSetToCustomer(resultSet, authorizationDataList));
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
                "WHERE id = (SELECT authorization_id FROM customer WHERE id = '" + id + "')",
                customer.getAuthorizationData().getEmail(),
                customer.getAuthorizationData().getUsername(),
                customer.getAuthorizationData().getPassword(),
                customer.getAuthorizationData().getFirstName(),
                customer.getAuthorizationData().getLastName(),
                customer.getAuthorizationData().getPhoneNumber());
    }

    @Override
    public void deleteById(UUID customerId) {
        jdbcTemplate.update("DELETE FROM authorization_data WHERE id = (" +
                "SELECT authorization_id FROM customer WHERE id = '" + customerId + "')");
        jdbcTemplate.update("DELETE FROM customer WHERE id = '" + customerId + "'");
    }

    @Override
    public Customer findById(UUID id) {
        List<Customer> customerList = findAll();
        List<Customer> filteredList = customerList.stream()
                .filter(customer -> customer.getId().equals(id)).collect(Collectors.toList());
        if(filteredList.size() > 1) try {
            throw new SQLException();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return filteredList.get(0);
    }

    public Customer findByUsername(String username) {
        List<Customer> customerList = findAll();
        List<Customer> filteredList = customerList.stream()
                .filter(customer -> customer.getAuthorizationData().getUsername().equals(username)).collect(Collectors.toList());
        if(filteredList.size() > 1) try {
            throw new SQLException();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return filteredList.get(0);
    }

    private Customer parseResultSetToCustomer(ResultSet resultSetCustomer, List<AuthorizationData> authorizationDataList) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSetCustomer.getObject("id", UUID.class));
        List<AuthorizationData> authorizationDataForCurrentCustomer = authorizationDataList.stream().filter(data -> {
            try {
                return data.getId().equals(resultSetCustomer.getObject("authorization_id", UUID.class));
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        customer.setAuthorizationData(authorizationDataForCurrentCustomer.get(0));
        return customer;
    }

    private AuthorizationData parseAuthorizationDataFromResultSet( ResultSet resultSetAuthorization) throws SQLException {
        AuthorizationData data = new AuthorizationData(
                resultSetAuthorization.getString("email"),
                resultSetAuthorization.getString("username"),
                resultSetAuthorization.getString("password"),
                resultSetAuthorization.getString("first_name"),
                resultSetAuthorization.getString("last_name"),
                resultSetAuthorization.getString("phone_number")
        );
        data.setId(resultSetAuthorization.getObject("id", UUID.class));
        return data;
    }
}
