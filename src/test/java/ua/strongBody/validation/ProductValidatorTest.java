package ua.strongBody.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.exceptions.ValidationException;
import ua.strongBody.models.Product;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

    private static final String PRODUCT_NAME1 = "prod1";
    private static final String PRODUCT_NAME2 = "prod2";
    private static final String ARTICLE1 = "PP-01";
    private static final String ARTICLE2 = "PP-02";

    private Product inputProduct;
    private List<Product> productList;

    @Mock
    private ProductDAO productDAO;

    @InjectMocks
    private ProductValidator testInstance;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setName(PRODUCT_NAME1);
        product.setArticle(ARTICLE1);

        productList = Collections.singletonList(product);

        inputProduct = new Product();
        inputProduct.setName(PRODUCT_NAME2);
        inputProduct.setArticle(ARTICLE2);
    }

    @Test
    void shouldValidate() throws ValidationException {
        when(productDAO.findAll()).thenReturn(productList);

        testInstance.validate(inputProduct);
    }

    @Test
    void shouldThrowExceptionOnSameName() {
        inputProduct.setName(PRODUCT_NAME1);
        when(productDAO.findAll()).thenReturn(productList);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(inputProduct));
    }

    @Test
    void shouldThrowExceptionOnSameArticle() {
        inputProduct.setArticle(ARTICLE1);
        when(productDAO.findAll()).thenReturn(productList);

        assertThrows(ValidationException.class, () ->
                testInstance.validate(inputProduct));
    }
}
