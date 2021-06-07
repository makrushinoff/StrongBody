package ua.strongBody.facade;

import org.springframework.stereotype.Component;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Order;
import ua.strongBody.populator.Populator;
import ua.strongBody.services.CartService;
import ua.strongBody.services.CustomerService;
import ua.strongBody.services.OrderService;

import java.util.List;
import java.util.UUID;

@Component
public class OrderFacadeImpl implements OrderFacade {

    private final CartService cartService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final Populator<Cart, Order> cartOrderPopulator;

    public OrderFacadeImpl(CartService cartService, CustomerService customerService, OrderService orderService, Populator<Cart, Order> cartOrderPopulator) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.cartOrderPopulator = cartOrderPopulator;
    }

    @Override
    public void createOrderFromCartByCustomerUsername(String username) {
        Customer customer = customerService.findByUsername(username);
        UUID customerId = customer.getId();
        Cart customerCart = cartService.findCartByCustomerId(customerId);
        Order order = new Order();
        cartOrderPopulator.convert(customerCart, order);
        orderService.save(order);
    }

    @Override
    public List<Order> getOrdersByCustomerUsername(String username) {
        Customer customer = customerService.findByUsername(username);
        return orderService.findOrdersByCustomerId(customer.getId());
    }

}
