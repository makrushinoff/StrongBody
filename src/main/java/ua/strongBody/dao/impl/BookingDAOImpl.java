package ua.strongBody.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.strongBody.assembly.BookingAssembly;
import ua.strongBody.assembly.CartAssembly;
import ua.strongBody.assembly.ProductAssembly;
import ua.strongBody.dao.BookingDAO;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BookingDAOImpl implements BookingDAO {

    private static final Logger LOG = LoggerFactory.getLogger(BookingDAOImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO booking(id, order_date, product_amount, order_number, product_id, cart_id)" +
            " VALUES('%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String UPDATE_QUERY = "UPDATE booking SET " +
            "order_date = '%s'," +
            "product_amount = '%s'," +
            "order_number = '%s'," +
            "product_id = '%s' " +
            "WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM booking WHERE id = '%s'";
    private static final String SAVE_WITHOUT_ID_QUERY = "INSERT INTO booking(order_date, product_amount, order_number, product_id, cart_id)" +
            " VALUES('%s', '%s', '%s', '%s', '%s')";

    private static final String PRODUCT_UNRESOLVED_EXCEPTION_PATTERN = "Booking with id: '%s' has unknown product! (product id: '%s')";
    private static final String CART_UNRESOLVED_EXCEPTION_PATTERN = "Booking with id: '%s' has unknown cart! (cart id: '%s')";

    private final JdbcTemplate jdbcTemplate;
    private final BookingAssembly bookingAssembly;
    private final ProductAssembly productAssembly;
    private final CartAssembly cartAssembly;

    public BookingDAOImpl(JdbcTemplate jdbcTemplate, BookingAssembly bookingAssembly, ProductAssembly productAssembly, CartAssembly cartAssembly) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookingAssembly = bookingAssembly;
        this.productAssembly = productAssembly;
        this.cartAssembly = cartAssembly;
    }

    @Override
    public List<Booking> findAll() throws DataAccessException {
        List<Booking> bookings = bookingAssembly.findAllSingleLayer();

        List<Product> allProducts = productAssembly.findAllProduct();
        bookings.forEach(booking -> mapProductToBooking(allProducts, booking));

        List<Cart> allCarts = cartAssembly.findAllCartsSingleLayer();
        bookings.forEach(booking -> mapCartToBooking(allCarts, booking));

        return bookings;
    }

    private void mapCartToBooking(List<Cart> carts, Booking booking) {
        UUID cartIdForBooking = booking.getCart().getId();

        Optional<Cart> cartForBookingOptional = carts.stream()
                .filter(cart -> isEqualsId(cartIdForBooking, cart))
                .findFirst();

        populateCartForBooking(booking, cartForBookingOptional);
    }

    private void populateCartForBooking(Booking booking, Optional<Cart> cartForBookingOptional) {
        if (cartForBookingOptional.isPresent()) {
            Cart cartForBooking = cartForBookingOptional.get();
            booking.setCart(cartForBooking);
            return;
        }
        processCartUnresolvedWarning(booking);
    }

    private void processCartUnresolvedWarning(Booking booking) {
        UUID bookingId = booking.getId();
        UUID cartIdForProduct = booking.getCart().getId();
        String message = String.format(CART_UNRESOLVED_EXCEPTION_PATTERN, bookingId, cartIdForProduct);
        LOG.warn(message);
    }

    private boolean isEqualsId(UUID cartIdForBooking, Cart cart) {
        return cart.getId().equals(cartIdForBooking);
    }

    private void mapProductToBooking(List<Product> products, Booking booking) {
        UUID productIdFormBooking = booking.getProduct().getId();

        Optional<Product> productForBookingOptional = products.stream()
                .filter(product -> isEqualsById(productIdFormBooking, product))
                .findFirst();
        populateProductForBooking(booking, productForBookingOptional);
    }

    private void populateProductForBooking(Booking booking, Optional<Product> productForBookingOptional) {
        if (productForBookingOptional.isPresent()) {
            Product productForBooking = productForBookingOptional.get();
            booking.setProduct(productForBooking);
            return;
        }
        processProductUnresolvedWarning(booking);
    }

    private void processProductUnresolvedWarning(Booking booking) {
        UUID bookingId = booking.getId();
        UUID productIdFromBooking = booking.getProduct().getId();
        String message = String.format(PRODUCT_UNRESOLVED_EXCEPTION_PATTERN, bookingId, productIdFromBooking);
        LOG.warn(message);
    }

    private boolean isEqualsById(UUID productIdFormBooking, Product product) {
        return product.getId().equals(productIdFormBooking);
    }

    @Override
    public void save(Booking booking) {
        jdbcTemplate.update(String.format(SAVE_QUERY,
                booking.getId(),
                booking.getOrderDate(),
                booking.getProductAmount(),
                booking.getOrderNumber(),
                booking.getProduct().getId(),
                booking.getCart().getId())
        );
    }

    @Override
    public void updateById(UUID id, Booking booking) {
        jdbcTemplate.update(String.format(UPDATE_QUERY,
                booking.getOrderDate(),
                booking.getProductAmount(),
                booking.getOrderNumber(),
                booking.getProduct().getId(),
                id)
        );
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update(String.format(DELETE_QUERY, id));
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return findAll().stream()
                .filter(booking -> isEqualsById(id, booking))
                .findFirst();
    }

    private boolean isEqualsById(UUID id, Booking booking) {
        return booking.getId().equals(id);
    }

    @Override
    public void saveWithoutId(Booking booking) {
        jdbcTemplate.update(String.format(SAVE_WITHOUT_ID_QUERY,
                booking.getOrderDate(),
                booking.getProductAmount(),
                booking.getOrderNumber(),
                booking.getProduct().getId(),
                booking.getCart().getId())
        );
    }

    @Override
    public List<Booking> getBookingsByCartId(UUID cartId) {
        return findAll().stream()
                .filter(booking -> isEqualsByCartId(cartId, booking))
                .collect(Collectors.toList());
    }

    private boolean isEqualsByCartId(UUID cartId, Booking booking) {
        return booking.getCart().getId().equals(cartId);
    }
}
