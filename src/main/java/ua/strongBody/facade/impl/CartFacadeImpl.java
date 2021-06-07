package ua.strongBody.facade.impl;

import org.springframework.stereotype.Component;
import ua.strongBody.facade.CartFacade;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Product;
import ua.strongBody.services.BookingService;
import ua.strongBody.services.CartService;
import ua.strongBody.services.CustomerService;
import ua.strongBody.services.ProductService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CartFacadeImpl implements CartFacade {

    private final CartService cartService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final BookingService bookingService;

    public CartFacadeImpl(CartService cartService, CustomerService customerService, ProductService productService, BookingService bookingService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
        this.bookingService = bookingService;
    }

    public void addProductToCartByCustomerUsername(UUID productId, String username) {
        Customer customer = customerService.findByUsername(username);
        Cart customerCart = cartService.findCartByCustomerId(customer.getId());
        Product reservedProduct = productService.findById(productId);
        List<Booking> customerBookings = bookingService.getCustomerBookingsByCartId(customerCart.getId());

        Optional<Booking> bookingById = customerBookings.stream().filter(booking -> booking.getProduct().getId().equals(reservedProduct.getId())).findFirst();
        bookingById.ifPresentOrElse((booking -> processExistingBooking(booking, reservedProduct)), () -> processNewBooking(reservedProduct, customerCart));
    }

    private void processExistingBooking(Booking booking, Product reservedProduct) {
        int newProductAmount = booking.getProductAmount() + 1;
        booking.setProductAmount(newProductAmount);
        bookingService.updateById(booking.getId(), booking);

        int newReservedAmount = reservedProduct.getReservedAmount() + 1;
        reservedProduct.setReservedAmount(newReservedAmount);
        productService.updateById(reservedProduct.getId(), reservedProduct);
    }

    private void processNewBooking(Product reservedProduct, Cart customerCart) {
        Booking newBooking = createNewBooking(reservedProduct, customerCart);
        bookingService.createBooking(newBooking);

        int newReservedAmount = reservedProduct.getReservedAmount() + 1;
        reservedProduct.setReservedAmount(newReservedAmount);
        productService.updateById(reservedProduct.getId(), reservedProduct);
    }

    private Booking createNewBooking(Product reservedProduct, Cart customerCart) {
        Booking newBooking = new Booking();
        newBooking.setProduct(reservedProduct);
        newBooking.setCart(customerCart);
        newBooking.setProductAmount(1);
        newBooking.setOrderNumber(UUID.randomUUID().toString());
        newBooking.setOrderDate(LocalDate.now());
        return newBooking;
    }

    public void removeBookingFromCart(UUID bookingId) {
        Booking bookingToDelete = bookingService.findById(bookingId);

        if (bookingToDelete.getProductAmount() == 1) {
            processCompleteRemove(bookingToDelete);
        } else {
            processSingleRemove(bookingToDelete);
        }
    }

    private void processCompleteRemove(Booking bookingToDelete) {
        bookingService.deleteById(bookingToDelete.getId());

        Product product = bookingToDelete.getProduct();
        int newReservedAmount = product.getReservedAmount() - 1;
        product.setReservedAmount(newReservedAmount);
        productService.updateById(product.getId(), product);
    }

    private void processSingleRemove(Booking bookingToDelete) {
        int newProductAmount = bookingToDelete.getProductAmount() - 1;
        bookingToDelete.setProductAmount(newProductAmount);
        bookingService.updateById(bookingToDelete.getId(), bookingToDelete);

        Product product = bookingToDelete.getProduct();
        int newReservedAmount = product.getReservedAmount() - 1;
        product.setReservedAmount(newReservedAmount);
        productService.updateById(product.getId(), product);
    }

    public void submitCartByCustomerUsername(String customerUsername) {
        Customer customer = customerService.findByUsername(customerUsername);
        Cart customerCart = cartService.findCartByCustomerId(customer.getId());
        List<Booking> customerBookings = bookingService.getCustomerBookingsByCartId(customerCart.getId());
        customerBookings.forEach(this::cleanBooking);
    }

    private void cleanBooking(Booking booking) {
        Product product = booking.getProduct();

        int newReservedAmount = product.getReservedAmount() - booking.getProductAmount();
        int newAmount = product.getAmount() - booking.getProductAmount();
        product.setReservedAmount(newReservedAmount);
        product.setAmount(newAmount);

        bookingService.deleteById(booking.getId());
        productService.updateById(product.getId(), product);
    }

    public List<Booking> getCustomerBookingsByUsername(String customerUsername) {
        Customer customer = customerService.findByUsername(customerUsername);
        Cart customerCart = cartService.findCartByCustomerId(customer.getId());
        UUID customerCartId = customerCart.getId();
        return bookingService.getCustomerBookingsByCartId(customerCartId);
    }

    @Override
    public Cart getCartByCustomerUsername(String customerUsername) {
        Customer customer = customerService.findByUsername(customerUsername);
        UUID customerId = customer.getId();
        return cartService.findCartByCustomerId(customerId);
    }
}
