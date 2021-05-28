package ua.strongBody.dao;

import ua.strongBody.models.Cart;

public interface CartDAO extends GeneralDAO<Cart> {
    void saveWithoutId(Cart cart);
}
