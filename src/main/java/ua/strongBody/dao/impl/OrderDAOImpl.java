package ua.strongBody.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.strongBody.assembly.CustomerAssembly;
import ua.strongBody.assembly.OrderAssembly;
import ua.strongBody.dao.OrderDAO;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private static final Logger LOG = LoggerFactory.getLogger(OrderDAOImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO order_table (order_date, product_amount, price, customer_id)" +
            " VALUES('%s', '%s', '%s', '%s')";
    private static final String UPDATE_QUERY = "UPDATE order_table SET " +
            "order_date = '%s'," +
            "product_amount = '%s'," +
            "price = '%s' WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM order_table WHERE id = '%s'";

    private static final String CUSTOMER_UNRESOLVED_EXCEPTION_PATTERN = "Order with id: '%s' has unknown customer! (customer id: '%s')";

    private final JdbcTemplate jdbcTemplate;
    private final OrderAssembly orderAssembly;
    private final CustomerAssembly customerAssembly;

    public OrderDAOImpl(JdbcTemplate jdbcTemplate, OrderAssembly orderAssembly, CustomerAssembly customerAssembly) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderAssembly = orderAssembly;
        this.customerAssembly = customerAssembly;
    }

    @Override
    public List<Order> findAll() throws DataAccessException {
        List<Order> orders = orderAssembly.findAllOrdersSingleLayer();

        List<Customer> allCustomers = customerAssembly.findAllCustomers();
        orders.forEach(order -> mapCustomerToOrder(allCustomers, order));

        return orders;
    }

    public void mapCustomerToOrder(List<Customer> customers, Order order) {
        UUID customerId = order.getCustomer().getId();

        Optional<Customer> customerOptional = customers.stream()
                .filter(customer -> customer.getId().equals(customerId))
                .findFirst();
        populateCustomerForOrder(order, customerOptional);
    }

    private void populateCustomerForOrder(Order order, Optional<Customer> customerOptional) {
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            order.setCustomer(customer);
            return;
        }
        processCustomerUnresolvedWarning(order);
    }

    private void processCustomerUnresolvedWarning(Order order) {
        UUID customerId = order.getCustomer().getId();
        String message = String.format(CUSTOMER_UNRESOLVED_EXCEPTION_PATTERN, order.getId(), customerId);
        LOG.warn(message);
    }

    @Override
    public void save(Order order) throws DataAccessException {
        String query = String.format(SAVE_QUERY,
                order.getOrderedDate(),
                order.getProductAmount(),
                order.getPrice(),
                order.getCustomer().getId());
        jdbcTemplate.update(query);
    }

    @Override
    public void updateById(UUID id, Order order) {
        String query = String.format(UPDATE_QUERY, order.getOrderedDate(), order.getProductAmount(), order.getPrice(), order.getId());
        jdbcTemplate.update(query);
    }

    @Override
    public void deleteById(UUID id) {
        String query = String.format(DELETE_QUERY, id);
        jdbcTemplate.update(query);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return findAll().stream().filter(product -> product.getId().equals(id)).findFirst();
    }

    @Override
    public List<Order> findOrdersByCustomerId(UUID customerId) {
        return findAll().stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }
}
