package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CartDAO;
import ua.strongBody.exceptions.IdNotFoundException;
import ua.strongBody.models.Cart;
import ua.strongBody.services.CartService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    private static final String GENERAL_CART_NOT_FOUND_PATTERN = "Cart with %s: '%s' not found!";

    private final CartDAO cartDAO;

    public CartServiceImpl(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public void save(Cart cart) {

    }

    @Override
    public void updateById(UUID id, Cart cart) {

    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public Cart findById(UUID id) {
        return null;
    }

    @Override
    public Cart findCartByCustomerId(UUID customerId) {
        Optional<Cart> customerOptional = cartDAO.findCartByCustomerId(customerId);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        }
        processCustomerIdNotFoundException(customerId);
        return null;
    }

    private void processCustomerIdNotFoundException(UUID id) {
        String message = String.format(GENERAL_CART_NOT_FOUND_PATTERN, Cart.CUSTOMER_ID_FIELD, id);
        LOG.warn(message);
        throw new IdNotFoundException(message);
    }
}
