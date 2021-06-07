package ua.strongBody.services;

import ua.strongBody.models.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService extends GeneralService<Order> {
    List<Order> findOrdersByCustomerId(UUID customerId);
}
