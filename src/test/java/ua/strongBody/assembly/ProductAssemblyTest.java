package ua.strongBody.assembly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.strongBody.models.Product;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAssemblyTest {

    private static final String FIND_ALL_QUERY = "SELECT * FROM product";

    private Product product;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RowMapper<Product> productRowMapper;

    @InjectMocks
    private ProductAssembly testInstance;

    @BeforeEach
    void setUp() {
        product = new Product();
        List<Product> products = Collections.singletonList(product);

        when(jdbcTemplate.query(FIND_ALL_QUERY, productRowMapper)).thenReturn(products);
    }

    @Test
    void shouldFindAllProduct() {
        List<Product> actual = testInstance.findAllProduct();

        assertThat(actual).contains(product);
    }
}
