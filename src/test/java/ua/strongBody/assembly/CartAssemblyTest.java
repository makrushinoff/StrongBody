package ua.strongBody.assembly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.strongBody.models.Cart;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartAssemblyTest {

    private static final String FIND_ALL_QUERY = "SELECT * FROM cart";

    private Cart cart;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RowMapper<Cart> cartRowMapper;

    @InjectMocks
    private CartAssembly testInstance;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        List<Cart> carts = Collections.singletonList(cart);

        when(jdbcTemplate.query(FIND_ALL_QUERY, cartRowMapper)).thenReturn(carts);
    }

    @Test
    void shouldFindAllCartsSingleLayer() {
        List<Cart> actual = testInstance.findAllCartsSingleLayer();

        assertThat(actual).contains(cart);
    }
}
