package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.OrderDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Order;
import ua.strongBody.services.OrderService;

import java.util.List;
import java.util.UUID;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public List<Order> findAll() {
        LOG.debug(LOG_DEBUG_EMPTY_PATTERN);
        return orderDAO.findAll();
    }

    @Override
    public void save(Order order) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, order);
        orderDAO.save(order);
    }

    @Override
    public void updateById(UUID id, Order order) {
        LOG.debug(LOG_DEBUG_TWO_ARG_PATTERN, id, order);
        orderDAO.updateById(id, order);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        orderDAO.deleteById(id);
    }

    @Override
    public Order findById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        return orderDAO.findById(id)
                .orElseThrow(() -> generateGeneralOrderException(Order.ID_FIELD, id.toString()));
    }

    private RuntimeException generateGeneralOrderException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_ORDER_NOT_FOUND_PATTERN, invalidField, invalidValue);
        LOG.warn(message);
        return new FieldNotFoundException(message);
    }

    @Override
    public List<Order> findOrdersByCustomerId(UUID customerId) {
        return orderDAO.findOrdersByCustomerId(customerId);
    }
}
