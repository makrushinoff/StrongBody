package ua.strongBody.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Product;
import ua.strongBody.services.BookingService;
import ua.strongBody.services.CartService;
import ua.strongBody.services.CustomerService;
import ua.strongBody.services.ProductService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final BookingService bookingService;

    public CartController(CartService cartService, CustomerService customerService, ProductService productService, BookingService bookingService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String getCartPage(Principal principal, Model model) {
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        Cart customerCart = cartService.findCartByCustomerId(customer.getId());
        List<Booking> customerBookings = bookingService.getCustomerBookingsByCartId(customerCart.getId());
        model.addAttribute("bookings", customerBookings);
        return "cart/cartView";
    }

    @GetMapping("/add/{productId}")
    public String addProductToCart(@PathVariable UUID productId, Principal principal) {
        try {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Cart customerCart = cartService.findCartByCustomerId(customer.getId());
            Product reservedProduct = productService.findById(productId);
            List<Booking> customerBookings = bookingService.getCustomerBookingsByCartId(customerCart.getId());
            Optional<Booking> booking = customerBookings.stream().filter(book -> book.getProduct().getId().equals(reservedProduct.getId())).findFirst();
            if (booking.isPresent()) {
                Booking currentBooking = booking.get();
                currentBooking.setProductAmount(currentBooking.getProductAmount() + 1);
                bookingService.updateById(currentBooking.getId(), currentBooking);
                reservedProduct.setReservedAmount(reservedProduct.getReservedAmount() + 1);
                productService.updateById(reservedProduct.getId(), reservedProduct);
                return "redirect:/catalog/";
            }

            Booking newBooking = new Booking();
            newBooking.setProduct(reservedProduct);
            newBooking.setCart(customerCart);
            newBooking.setProductAmount(1);
            newBooking.setOrderNumber(UUID.randomUUID().toString());
            newBooking.setOrderDate(LocalDate.now());
            bookingService.createBooking(newBooking);
            reservedProduct.setReservedAmount(reservedProduct.getReservedAmount() + 1);
            productService.updateById(reservedProduct.getId(), reservedProduct);
        } catch (FieldNotFoundException ex) {
            return "redirect:/";
        }

        return "redirect:/catalog/";
    }

    @GetMapping("/delete/{bookingId}")
    public String removeBooking(@PathVariable UUID bookingId) {
        Booking bookingToDelete = bookingService.findById(bookingId);
        if (bookingToDelete.getProductAmount() == 1) {
            bookingService.deleteById(bookingToDelete.getId());
            Product product = bookingToDelete.getProduct();
            product.setReservedAmount(product.getReservedAmount() - 1);
            productService.updateById(product.getId(), product);
            return "redirect:/cart/";
        }
        bookingToDelete.setProductAmount(bookingToDelete.getProductAmount() - 1);
        bookingService.updateById(bookingToDelete.getId(), bookingToDelete);
        Product product = bookingToDelete.getProduct();
        product.setReservedAmount(product.getReservedAmount() - 1);
        productService.updateById(product.getId(), product);
        return "redirect:/cart/";
    }

    @GetMapping("/submit")
    public String submitCart(Principal principal) {
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        Cart customerCart = cartService.findCartByCustomerId(customer.getId());
        List<Booking> customerBookings = bookingService.getCustomerBookingsByCartId(customerCart.getId());
        customerBookings.forEach(this::cleanBooking);
        return "cart/cartSubmit";
    }

    private void cleanBooking(Booking booking) {
        Product product = booking.getProduct();
        int newReservedAmount = product.getReservedAmount() - booking.getProductAmount();
        int newAvailableAmount = product.getAvailableAmount() - booking.getProductAmount();
        product.setReservedAmount(newReservedAmount);
        product.setAvailableAmount(newAvailableAmount);
        bookingService.deleteById(booking.getId());
        productService.updateById(product.getId(), product);
    }
}
