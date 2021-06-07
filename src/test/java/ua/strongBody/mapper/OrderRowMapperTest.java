package ua.strongBody.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.models.Order;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ua.strongBody.models.Order.*;

@ExtendWith(MockitoExtension.class)
class OrderRowMapperTest {

    private static final int ROW_NUM = 10;
    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final LocalDate ORDERED_DATE = LocalDate.now();
    private static final int PRODUCT_AMOUNT = 20;
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    @Mock
    private ResultSet resultSet;

    private final OrderRowMapper orderRowMapper = new OrderRowMapper();

    @BeforeEach
    void setUp() throws SQLException {
        when(resultSet.getObject(Order.ID_FIELD, UUID.class)).thenReturn(ORDER_ID);
        when(resultSet.getObject(ORDER_DATE_FIELD, LocalDate.class)).thenReturn(ORDERED_DATE);
        when(resultSet.getInt(PRODUCT_AMOUNT_FIELD)).thenReturn(PRODUCT_AMOUNT);
        when(resultSet.getBigDecimal(PRICE_FIELD)).thenReturn(PRICE);
        when(resultSet.getObject(CUSTOMER_ID_FIELD, UUID.class)).thenReturn(CUSTOMER_ID);
    }

    @Test
    void shouldMapRow() throws SQLException {
        Order actual = orderRowMapper.mapRow(resultSet, ROW_NUM);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ORDER_ID);
        assertThat(actual.getOrderedDate()).isEqualTo(ORDERED_DATE);
        assertThat(actual.getProductAmount()).isEqualTo(PRODUCT_AMOUNT);
        assertThat(actual.getPrice()).isEqualTo(PRICE);
        assertThat(actual.getCustomer().getId()).isEqualTo(CUSTOMER_ID);
    }
}
