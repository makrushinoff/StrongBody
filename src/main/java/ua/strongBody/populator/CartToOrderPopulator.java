package ua.strongBody.populator;

import org.springframework.stereotype.Component;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Order;

import java.time.LocalDate;

@Component
public class CartToOrderPopulator implements Populator<Cart, Order> {

    @Override
    public void convert(Cart cart, Order order) {
        order.setOrderedDate(LocalDate.now());
        order.setProductAmount(cart.getQuantity());
        order.setPrice(cart.getPrice());
        order.setCustomer(cart.getCustomer());
    }
}
