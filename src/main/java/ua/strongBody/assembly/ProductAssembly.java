package ua.strongBody.assembly;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Product;

import java.util.List;

@Component
public class ProductAssembly {

    private static final String FIND_ALL_QUERY = "SELECT * FROM product";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> productRowMapper;

    public ProductAssembly(JdbcTemplate jdbcTemplate, RowMapper<Product> productRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRowMapper = productRowMapper;
    }

    public List<Product> findAllProduct() {
        return jdbcTemplate.query(FIND_ALL_QUERY, productRowMapper);
    }
}
