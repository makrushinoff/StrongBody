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
        Optional<Cart> cartOptional = cartDAO.findById(id);
        if (cartOptional.isPresent()) {
            return cartOptional.get();
        }
        processGeneralCartException(Cart.ID_FIELD, id.toString());
        return null;
    }

    @Override
    public Cart findCartByCustomerId(UUID customerId) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, customerId);
        Optional<Cart> customerOptional = cartDAO.findCartByCustomerId(customerId);
        if (customerOptional.isPresent()) {
            return customerOptional.get();
        }
        processGeneralCartException(Cart.CUSTOMER_ID_FIELD, customerId.toString());
        return null;
    }

    private void processGeneralCartException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_CART_NOT_FOUND_PATTERN, invalidField, invalidValue);
        LOG.warn(message);
        throw new IdNotFoundException(message);
    }
}
