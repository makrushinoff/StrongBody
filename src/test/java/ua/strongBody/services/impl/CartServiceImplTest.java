package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.CartDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    private static final UUID ID = UUID.randomUUID();

    private Cart cart;

    @Mock
    private CartDAO cartDAO;

    @InjectMocks
    private CartServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setCustomer(new Customer());
    }

    @Test
    void shouldFindAll() {
        testInstance.findAll();

        verify(cartDAO).findAll();
    }

    @Test
    void shouldSave() {
        testInstance.save(cart);

        verify(cartDAO).save(cart);
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ID, cart);

        verify(cartDAO).updateById(ID, cart);
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ID);

        verify(cartDAO).deleteById(ID);
    }

    @Test
    void shouldFindById() {
        Optional<Cart> cartOptional = Optional.of(cart);
        when(cartDAO.findById(ID)).thenReturn(cartOptional);

        Cart actual = testInstance.findById(ID);

        assertThat(actual).isEqualTo(cart);
    }

    @Test
    void shouldProcessExportExceptionOnInvalidId() {
        when(cartDAO.findById(ID)).thenReturn(Optional.empty());

        assertThrows(FieldNotFoundException.class, () -> testInstance.findById(ID));
    }

    @Test
    void shouldFindCartByCustomerId() {
        Optional<Cart> cartOptional = Optional.of(cart);
        when(cartDAO.findCartByCustomerId(ID)).thenReturn(cartOptional);

        Cart actual = testInstance.findCartByCustomerId(ID);

        assertThat(actual).isEqualTo(cart);
    }

    @Test
    void shouldProcessExportExceptionOnInvalidCustomerId() {
        when(cartDAO.findCartByCustomerId(ID)).thenReturn(Optional.empty());

        assertThrows(FieldNotFoundException.class, () -> testInstance.findCartByCustomerId(ID));
    }
}
