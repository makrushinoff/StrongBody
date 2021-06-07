package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Product;
import ua.strongBody.processors.post.PostProcessor;
import ua.strongBody.services.ProductService;

import java.util.List;
import java.util.UUID;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;
    private final PostProcessor<Product> productPostProcessor;

    public ProductServiceImpl(ProductDAO productDAO, PostProcessor<Product> productPostProcessor) {
        this.productDAO = productDAO;
        this.productPostProcessor = productPostProcessor;
    }

    @Override
    public List<Product> findAll() {
        LOG.debug(LOG_DEBUG_EMPTY_PATTERN.getMessage());
        List<Product> allProducts = productDAO.findAll();
        productPostProcessor.postProcess(allProducts);
        return allProducts;
    }

    @Override
    public void save(Product product) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), product);
        productDAO.save(product);
    }

    @Override
    public void updateById(UUID id, Product product) {
        LOG.debug(LOG_DEBUG_TWO_ARG_PATTERN.getMessage(), product, id);
        productDAO.updateById(id, product);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), id);
        productDAO.deleteById(id);
    }

    @Override
    public Product findById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), id);
        Product product = productDAO.findById(id)
                .orElseThrow(() -> generateGeneralProductException(Product.ID_FIELD, id.toString()));
        productPostProcessor.postProcess(product);
        return product;
    }

    private RuntimeException generateGeneralProductException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_PRODUCT_NOT_FOUND_PATTERN.getMessage(), invalidField, invalidValue);
        LOG.warn(message);
        return new FieldNotFoundException(message);
    }

    @Override
    public void createProduct(Product product) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN.getMessage(), product);
        productDAO.saveWithoutId(product);
    }
}
