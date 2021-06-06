package ua.strongBody.facade;

import ua.strongBody.models.Booking;
import ua.strongBody.models.Cart;

import java.util.List;
import java.util.UUID;

public interface CartFacade {
    void addProductToCartByCustomerUsername(UUID productId, String username);

    void removeBookingFromCart(UUID bookingId);

    void submitCartByCustomerUsername(String customerUsername);

    List<Booking> getCustomerBookingsByUsername(String customerUsername);

    Cart getCartByCustomerUsername(String customerUsername);
}
