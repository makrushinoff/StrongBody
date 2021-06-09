package ua.strongBody.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.strongBody.assembly.ProductAssembly;
import ua.strongBody.models.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDAOImplUnitTest {

    private static final String SAVE_QUERY = "INSERT INTO product (id, name, price, article, description, amount)" +
            " VALUES('%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String SAVE_WITHOUT_ID_QUERY = "INSERT INTO product (name, price, article, description, amount)" +
            " VALUES('%s', '%s', '%s', '%s', '%s')";
    private static final String UPDATE_QUERY = "UPDATE product SET " +
            "name = '%s'," +
            "price = '%s'," +
            "article = '%s'," +
            "description = '%s'," +
            "amount = '%s'," +
            "reserved_amount = '%s' WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM product WHERE id = '%s'";

    private static final String PRODUCT_NAME = "prod1";
    private static final BigDecimal PRICE = BigDecimal.valueOf(2000);
    private static final String ARTICLE = "PP-01";
    private static final String DESCRIPTION = "Super-puper product1";
    private static final int AVAILABLE_AMOUNT = 5;
    private static final int RESERVED_AMOUNT = 0;
    private static final UUID ID = UUID.randomUUID();

    private Product product;
    private List<Product> productList;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ProductAssembly productAssembly;

    @InjectMocks
    private ProductDAOImpl testInstance;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(ID);
        product.setName(PRODUCT_NAME);
        product.setPrice(PRICE);
        product.setArticle(ARTICLE);
        product.setDescription(DESCRIPTION);
        product.setAmount(AVAILABLE_AMOUNT);

        productList = Collections.singletonList(product);
    }

    @Test
    void shouldFindAll() {
        when(productAssembly.findAllProduct()).thenReturn(productList);

        List<Product> actualList = testInstance.findAll();

        assertThat(actualList).isEqualTo(productList);
        assertThat(actualList.get(0)).isEqualTo(product);
    }

    @Test
    void shouldSave() {
        testInstance.save(product);

        verify(jdbcTemplate).update(String.format(SAVE_QUERY,
                ID,
                PRODUCT_NAME,
                PRICE,
                ARTICLE,
                DESCRIPTION,
                AVAILABLE_AMOUNT)
        );
    }

    @Test
    void shouldSaveWithoutId() {
        testInstance.saveWithoutId(product);

        verify(jdbcTemplate).update(String.format(SAVE_WITHOUT_ID_QUERY,
                PRODUCT_NAME,
                PRICE,
                ARTICLE,
                DESCRIPTION,
                AVAILABLE_AMOUNT)
        );
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ID, product);

        verify(jdbcTemplate).update(String.format(UPDATE_QUERY,
                PRODUCT_NAME,
                PRICE,
                ARTICLE,
                DESCRIPTION,
                AVAILABLE_AMOUNT,
                RESERVED_AMOUNT,
                ID)
        );
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ID);

        verify(jdbcTemplate).update(String.format(DELETE_QUERY, ID));
    }

    @Test
    void shouldFindById() {
        when(productAssembly.findAllProduct()).thenReturn(productList);

        Optional<Product> actualOptional = testInstance.findById(ID);

        assertThat(actualOptional).isPresent();
        Product actual = actualOptional.get();
        assertThat(actual).isEqualTo(productList.get(0));
    }

    @Test
    void shouldFindByArticle() {
        when(productAssembly.findAllProduct()).thenReturn(productList);

        Optional<Product> actualOptional = testInstance.findByArticle(ARTICLE);

        assertThat(actualOptional).isPresent();
        Product actual = actualOptional.get();
        assertThat(actual).isEqualTo(productList.get(0));
    }
}
