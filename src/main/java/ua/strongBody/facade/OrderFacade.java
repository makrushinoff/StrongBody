package ua.strongBody.facade;

import ua.strongBody.models.Order;

import java.util.List;

public interface OrderFacade {

    void createOrderFromCartByCustomerUsername(String username);

    List<Order> getOrdersByCustomerUsername(String username);
}
