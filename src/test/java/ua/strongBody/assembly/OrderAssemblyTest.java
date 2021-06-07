package ua.strongBody.assembly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.strongBody.models.Order;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderAssemblyTest {

    private static final String FIND_ALL_QUERY = "SELECT * FROM order_table";

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RowMapper<Order> orderRowMapper;

    @InjectMocks
    private OrderAssembly testInstance;

    @Test
    void shouldFindAllOrdersSingleLayer() {
        testInstance.findAllOrdersSingleLayer();

        verify(jdbcTemplate).query(FIND_ALL_QUERY, orderRowMapper);
    }
}
