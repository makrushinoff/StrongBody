package ua.strongBody.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.models.Cart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ua.strongBody.models.Cart.CUSTOMER_ID_FIELD;
import static ua.strongBody.models.Cart.ID_FIELD;

@ExtendWith(MockitoExtension.class)
class CartRowMapperTest {

    private static final int ROW_NUM = 10;
    private static final UUID CART_ID = UUID.randomUUID();
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    @Mock
    private ResultSet resultSet;

    private final CartRowMapper testInstance = new CartRowMapper();

    @BeforeEach
    void setUp() throws SQLException {
        when(resultSet.getObject(ID_FIELD, UUID.class)).thenReturn(CART_ID);
        when(resultSet.getObject(CUSTOMER_ID_FIELD, UUID.class)).thenReturn(CUSTOMER_ID);
    }

    @Test
    void shouldMapRow() throws SQLException {
        Cart actual = testInstance.mapRow(resultSet, ROW_NUM);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(CART_ID);
        assertThat(actual.getCustomer().getId()).isEqualTo(CUSTOMER_ID);
    }
}
