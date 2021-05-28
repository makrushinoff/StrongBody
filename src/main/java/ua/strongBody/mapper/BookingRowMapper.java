package ua.strongBody.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static ua.strongBody.models.Booking.*;

@Component
public class BookingRowMapper implements RowMapper<Booking> {

    @Override
    public Booking mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getObject(ID_FIELD, UUID.class));
        booking.setOrderDate(resultSet.getObject(ORDER_DATE_FIELD, LocalDate.class));
        booking.setProductAmount(resultSet.getInt(PRODUCT_AMOUNT_FIELD));
        booking.setOrderNumber(resultSet.getString(ORDER_NUMBER_FIELD));

        Product product = new Product();
        product.setId(resultSet.getObject(PRODUCT_ID_FIELD, UUID.class));
        booking.setProduct(product);

        Cart cart = new Cart();
        cart.setId(resultSet.getObject(CART_ID_FIELD, UUID.class));
        booking.setCart(cart);

        return booking;
    }
}
