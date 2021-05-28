package ua.strongBody.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.models.Booking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ua.strongBody.models.Booking.*;

@ExtendWith(MockitoExtension.class)
class BookingRowMapperTest {

    private static final int ROW_NUM = 10;
    private static final UUID BOOKING_ID = UUID.randomUUID();
    private static final LocalDate ORDER_DATE = LocalDate.of(2021, 10, 28);
    private static final int PRODUCT_AMOUNT = 10;
    private static final String ORDER_NUMBER = "12345";
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final UUID CART_ID = UUID.randomUUID();

    @Mock
    private ResultSet resultSet;

    private final BookingRowMapper testInstance = new BookingRowMapper();

    @BeforeEach
    void setUp() throws SQLException {
        when(resultSet.getObject(ID_FIELD, UUID.class)).thenReturn(BOOKING_ID);
        when(resultSet.getObject(ORDER_DATE_FIELD, LocalDate.class)).thenReturn(ORDER_DATE);
        when(resultSet.getInt(PRODUCT_AMOUNT_FIELD)).thenReturn(PRODUCT_AMOUNT);
        when(resultSet.getString(ORDER_NUMBER_FIELD)).thenReturn(ORDER_NUMBER);
        when(resultSet.getObject(PRODUCT_ID_FIELD, UUID.class)).thenReturn(PRODUCT_ID);
        when(resultSet.getObject(CART_ID_FIELD, UUID.class)).thenReturn(CART_ID);
    }

    @Test
    void shouldMapRow() throws SQLException {
        Booking actual = testInstance.mapRow(resultSet, ROW_NUM);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(BOOKING_ID);
        assertThat(actual.getOrderDate()).isEqualTo(ORDER_DATE);
        assertThat(actual.getProductAmount()).isEqualTo(PRODUCT_AMOUNT);
        assertThat(actual.getOrderNumber()).isEqualTo(ORDER_NUMBER);
        assertThat(actual.getProduct().getId()).isEqualTo(PRODUCT_ID);
        assertThat(actual.getCart().getId()).isEqualTo(CART_ID);
    }
}
