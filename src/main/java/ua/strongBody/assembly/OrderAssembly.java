package ua.strongBody.assembly;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Order;

import java.util.List;

@Component
public class OrderAssembly {

    private static final String FIND_ALL_QUERY = "SELECT * FROM order_table";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Order> orderRowMapper;

    public OrderAssembly(JdbcTemplate jdbcTemplate, RowMapper<Order> orderRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRowMapper = orderRowMapper;
    }

    public List<Order> findAllOrdersSingleLayer() {
        return jdbcTemplate.query(FIND_ALL_QUERY, orderRowMapper);
    }
}
