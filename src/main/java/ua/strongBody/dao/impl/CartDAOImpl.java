package ua.strongBody.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.strongBody.assembly.BookingAssembly;
import ua.strongBody.assembly.CartAssembly;
import ua.strongBody.assembly.CustomerAssembly;
import ua.strongBody.dao.CartDAO;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public class CartDAOImpl implements CartDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CartDAOImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO cart (id, customer_id) VALUES ('%s' , '%s')";
    private static final String SAVE_WITHOUT_ID_QUERY = "INSERT INTO cart (customer_id) VALUES ('%s')";
    private static final String UPDATE_QUERY = "UPDATE cart SET customer_id = '%s' WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM cart WHERE id = '%s'";

    private static final String CUSTOMER_UNRESOLVED_EXCEPTION_PATTERN = "Cart with id: '%s' has unknown customer! (customer id: '%s')";

    private final JdbcTemplate jdbcTemplate;
    private final CartAssembly cartAssembly;
    private final CustomerAssembly customerAssembly;
    private final BookingAssembly bookingAssembly;

    public CartDAOImpl(JdbcTemplate jdbcTemplate, CartAssembly cartAssembly, CustomerAssembly customerAssembly, BookingAssembly bookingAssembly) {
        this.jdbcTemplate = jdbcTemplate;
        this.cartAssembly = cartAssembly;
        this.customerAssembly = customerAssembly;
        this.bookingAssembly = bookingAssembly;
    }

    /**
     * Definition:
     * One layer -> class with only table data.
     * Two layer -> class with consist of own table fields and second layer.
     * <p>
     * Two layer object in this method is Cart.
     * Cart table consist of fields: id and cart id.
     * First field is simple. Second Field is object.
     * <p>
     * At first jdbc template query we get single layer {@link ua.strongBody.mapper.CartRowMapper}.
     * And then we populate Customer field (second layer).
     *
     * @return List with two layered carts
     */
    @Override
    public List<Cart> findAll() throws DataAccessException {
        List<Cart> carts = cartAssembly.findAllCartsSingleLayer();

        List<Customer> allCustomers = customerAssembly.findAllCustomers();
        carts.forEach(cart -> mapCustomerToCart(allCustomers, cart));

        List<Booking> allBookings = bookingAssembly.findAllSingleLayer();
        carts.forEach(cart -> mapBookingsToCart(allBookings, cart));

        return carts;
    }

    private void mapCustomerToCart(List<Customer> allCustomers, Cart cart) {
        UUID customerIdFromCart = cart.getCustomer().getId();

        Optional<Customer> customerForCartOptional = allCustomers.stream()
                .filter(customer -> isEqualsCustomerId(customerIdFromCart, customer))
                .findFirst();
        populateCustomerForCart(cart, customerForCartOptional);
    }

    private void populateCustomerForCart(Cart cart, Optional<Customer> customerForCartOptional) {
        if (customerForCartOptional.isPresent()) {
            Customer customerForCart = customerForCartOptional.get();
            cart.setCustomer(customerForCart);
            return;
        }
        processCustomerUnresolvedWarning(cart);
    }

    private void processCustomerUnresolvedWarning(Cart cart) {
        UUID customerIdFromCart = cart.getCustomer().getId();
        String message = String.format(CUSTOMER_UNRESOLVED_EXCEPTION_PATTERN, cart.getId(), customerIdFromCart);
        LOG.warn(message);
    }

    private void mapBookingsToCart(List<Booking> allBookings, Cart cart) {
        UUID cartId = cart.getId();
        List<Booking> bookingsForCart = allBookings.stream()
                .filter(booking -> booking.getCart().getId().equals(cartId))
                .collect(Collectors.toList());
        cart.setBookingList(bookingsForCart);
    }

    private boolean isEqualsCustomerId(UUID customerIdFromCart, Customer customer) {
        return customer.getId().equals(customerIdFromCart);
    }

    @Override
    public void save(Cart cart) {
        UUID customerId = cart.getCustomer().getId();
        jdbcTemplate.update(String.format(SAVE_QUERY, cart.getId(), customerId));
    }

    @Override
    public void saveWithoutId(Cart cart) {
        UUID customerId = cart.getCustomer().getId();
        jdbcTemplate.update(String.format(SAVE_WITHOUT_ID_QUERY, customerId));
    }

    @Override
    public Optional<Cart> findCartByCustomerId(UUID customerId) {
        return findAll().stream().filter(cart -> isEqualsByCustomerId(customerId, cart)).findFirst();
    }

    private boolean isEqualsByCustomerId(UUID customerId, Cart cart) {
        return cart.getCustomer().getId().equals(customerId);
    }

    @Override
    public void updateById(UUID id, Cart cart) {
        UUID customerId = cart.getCustomer().getId();
        jdbcTemplate.update(String.format(UPDATE_QUERY, customerId, id));
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update(String.format(DELETE_QUERY, id));
    }

    @Override
    public Optional<Cart> findById(UUID id) {
        return findAll().stream()
                .filter(cart -> isEqualsById(id, cart))
                .findFirst();
    }

    private boolean isEqualsById(UUID id, Cart cart) {
        return cart.getId().equals(id);
    }
}
