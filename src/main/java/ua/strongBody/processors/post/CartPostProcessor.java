package ua.strongBody.processors.post;

import org.springframework.stereotype.Component;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;
import ua.strongBody.services.BookingService;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartPostProcessor implements PostProcessor<Cart> {

    private final BookingService bookingService;

    public CartPostProcessor(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void postProcess(List<Cart> carts) {
        carts.forEach(this::postProcess);
    }

    @Override
    public void postProcess(Cart cart) {
        calculateQuantity(cart);
        calculatePrice(cart);
    }

    private void calculateQuantity(Cart cart) {
        List<Booking> bookingList = cart.getBookingList();
        int sum = bookingList.stream().map(Booking::getProductAmount).reduce(0, Integer::sum);
        cart.setQuantity(sum);
    }

    private void calculatePrice(Cart cart) {
        List<Booking> cartBookings = bookingService.getCustomerBookingsByCartId(cart.getId());
        BigDecimal price = cartBookings.stream().map(Booking::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setPrice(price);
    }
}
