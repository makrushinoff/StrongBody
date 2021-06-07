package ua.strongBody.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static ua.strongBody.models.Order.*;

@Component
public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getObject(ID_FIELD, UUID.class));
        order.setOrderedDate(resultSet.getObject(ORDER_DATE_FIELD, LocalDate.class));
        order.setProductAmount(resultSet.getInt(PRODUCT_AMOUNT_FIELD));
        order.setPrice(resultSet.getBigDecimal(PRICE_FIELD));

        Customer customer = new Customer();
        customer.setId(resultSet.getObject(CUSTOMER_ID_FIELD, UUID.class));
        order.setCustomer(customer);

        return order;
    }
}
