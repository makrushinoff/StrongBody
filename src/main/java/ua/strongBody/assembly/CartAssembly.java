package ua.strongBody.assembly;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Cart;

import java.util.List;

@Component
public class CartAssembly {

    private static final String FIND_ALL_QUERY = "SELECT * FROM cart";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Cart> cartRowMapper;

    public CartAssembly(JdbcTemplate jdbcTemplate, RowMapper<Cart> cartRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.cartRowMapper = cartRowMapper;
    }

    public List<Cart> findAllCartsSingleLayer() {
        return jdbcTemplate.query(FIND_ALL_QUERY, cartRowMapper);
    }
}
