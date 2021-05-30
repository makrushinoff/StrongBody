package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Product;
import ua.strongBody.processors.post.PostProcessor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final UUID ID = UUID.randomUUID();

    private Product product;
    private List<Product> productList;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private PostProcessor<Product> productPostProcessor;

    @InjectMocks
    private ProductServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        product = new Product();
        productList = Collections.singletonList(product);
    }

    @Test
    void shouldFindAll() {
        when(productDAO.findAll()).thenReturn(productList);

        testInstance.findAll();

        verify(productPostProcessor).postProcess(productList);
    }

    @Test
    void shouldSave() {
        testInstance.save(product);

        verify(productDAO).save(product);
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ID, product);

        verify(productDAO).updateById(ID, product);
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ID);

        verify(productDAO).deleteById(ID);
    }

    @Test
    void shouldFindById() {
        Optional<Product> productOptional = Optional.of(product);
        when(productDAO.findById(ID)).thenReturn(productOptional);

        Product actual = testInstance.findById(ID);

        assertThat(actual).isEqualTo(product);
    }

    @Test
    void shouldProcessExportExceptionOnInvalidId() {
        when(productDAO.findById(ID)).thenReturn(Optional.empty());

        assertThrows(FieldNotFoundException.class, () -> testInstance.findById(ID));
    }

    @Test
    void shouldCreateProduct() {
        testInstance.createProduct(product);

        verify(productDAO).saveWithoutId(product);
    }
}
