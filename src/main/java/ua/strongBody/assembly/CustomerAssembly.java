package ua.strongBody.assembly;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Customer;

import java.util.List;

@Component
public class CustomerAssembly {

    private static final String FIND_ALL_QUERY = "SELECT * FROM customer";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Customer> customerRowMapper;

    public CustomerAssembly(JdbcTemplate jdbcTemplate, RowMapper<Customer> customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    public List<Customer> findAllCustomers() {
        return jdbcTemplate.query(FIND_ALL_QUERY, customerRowMapper);
    }
}
