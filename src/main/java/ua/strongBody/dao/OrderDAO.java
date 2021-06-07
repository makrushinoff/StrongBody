package ua.strongBody.dao;

import ua.strongBody.models.Order;

import java.util.List;
import java.util.UUID;

public interface OrderDAO extends GeneralDAO<Order> {

    List<Order> findOrdersByCustomerId(UUID customerId);
}
