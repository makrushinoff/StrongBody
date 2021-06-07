package ua.strongBody.dao;

import ua.strongBody.models.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartDAO extends GeneralDAO<Cart> {

    void saveWithoutId(Cart cart);

    Optional<Cart> findCartByCustomerId(UUID customerId);
}
