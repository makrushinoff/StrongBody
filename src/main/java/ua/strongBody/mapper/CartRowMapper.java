package ua.strongBody.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static ua.strongBody.models.Cart.*;

@Component
public class CartRowMapper implements RowMapper<Cart> {
    @Override
    public Cart mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Cart cart = new Cart();
        cart.setId(resultSet.getObject(ID_FIELD, UUID.class));

        Customer customer = new Customer();
        customer.setId(resultSet.getObject(CUSTOMER_ID_FIELD, UUID.class));

        cart.setCustomer(customer);
        return cart;
    }
}
