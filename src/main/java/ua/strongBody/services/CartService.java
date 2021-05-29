package ua.strongBody.services;

import ua.strongBody.models.Cart;

import java.util.UUID;

public interface CartService extends GeneralService<Cart> {
    Cart findCartByCustomerId(UUID customerId);
}
