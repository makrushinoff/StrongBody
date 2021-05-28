package ua.strongBody.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static ua.strongBody.models.Customer.*;

@Component
public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getObject(ID_FIELD, UUID.class));
        customer.setEmail(resultSet.getString(EMAIL_FIELD));
        customer.setFirstName(resultSet.getString(FIRST_NAME_FIELD));
        customer.setLastName(resultSet.getString(LAST_NAME_FIELD));
        customer.setPassword(resultSet.getString(PASSWORD_FIELD));
        customer.setUsername(resultSet.getString(USERNAME_FIELD));
        customer.setPhoneNumber(resultSet.getString(PHONE_NUMBER_FIELD));

        return customer;
    }
}
