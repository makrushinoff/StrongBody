package ua.strongBody.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ua.strongBody.models.Product.*;

@ExtendWith(MockitoExtension.class)
class ProductRowMapperTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String PRODUCT_NAME = "prod1";
    private static final int PRICE = 2000;
    private static final String ARTICLE = "PP-01";
    private static final String DESCRIPTION = "Super-puper product1";
    private static final int AVAILABLE_AMOUNT = 5;
    private static final int RESERVED_AMOUNT = 5;

    private static final int ROW_NUM = 10;

    @Mock
    private ResultSet resultSet;

    private final ProductRowMapper testInstance = new ProductRowMapper();

    @BeforeEach
    void setUp() throws SQLException {
        when(resultSet.getObject(ID_FIELD, UUID.class)).thenReturn(ID);
        when(resultSet.getString(NAME_FIELD)).thenReturn(PRODUCT_NAME);
        when(resultSet.getInt(PRICE_FIELD)).thenReturn(PRICE);
        when(resultSet.getString(ARTICLE_FIELD)).thenReturn(ARTICLE);
        when(resultSet.getString(DESCRIPTION_FIELD)).thenReturn(DESCRIPTION);
        when(resultSet.getInt(AVAILABLE_AMOUNT_FIELD)).thenReturn(AVAILABLE_AMOUNT);
        when(resultSet.getInt(RESERVED_AMOUNT_FIELD)).thenReturn(RESERVED_AMOUNT);
    }

    @Test
    void shouldMapRow() throws SQLException {
        Product actual = testInstance.mapRow(resultSet, ROW_NUM);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(actual.getPrice()).isEqualTo(PRICE);
        assertThat(actual.getArticle()).isEqualTo(ARTICLE);
        assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(actual.getAvailableAmount()).isEqualTo(AVAILABLE_AMOUNT);
        assertThat(actual.getReservedAmount()).isEqualTo(RESERVED_AMOUNT);
    }
}
