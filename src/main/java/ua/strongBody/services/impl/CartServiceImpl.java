package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.CartDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Cart;
import ua.strongBody.services.CartService;

import java.util.List;
import java.util.UUID;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartDAO cartDAO;

    public CartServiceImpl(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Override
    public List<Cart> findAll() {
        LOG.debug(LOG_DEBUG_EMPTY_PATTERN);
        return cartDAO.findAll();
    }

    @Override
    public void save(Cart cart) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, cart);
        cartDAO.save(cart);
    }

    @Override
    public void updateById(UUID id, Cart cart) {
        LOG.debug(LOG_DEBUG_TWO_ARG_PATTERN, cart, id);
        cartDAO.updateById(id, cart);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        cartDAO.deleteById(id);
    }

    @Override
    public Cart findById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        return cartDAO.findById(id)
                .orElseThrow(() -> generateGeneralCartException(Cart.ID_FIELD, id.toString()));
    }

    @Override
    public Cart findCartByCustomerId(UUID customerId) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, customerId);
        return cartDAO.findCartByCustomerId(customerId)
                .orElseThrow(() -> generateGeneralCartException(Cart.CUSTOMER_ID_FIELD, customerId.toString()));
    }

    private RuntimeException generateGeneralCartException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_CART_NOT_FOUND_PATTERN, invalidField, invalidValue);
        LOG.warn(message);
        return new FieldNotFoundException(message);
    }
}
