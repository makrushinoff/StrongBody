package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Product;
import ua.strongBody.services.ProductService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ua.strongBody.constants.LoggingConstants.*;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> findAll() {
        LOG.debug(LOG_DEBUG_EMPTY_PATTERN);
        List<Product> allProducts = productDAO.findAll();
        processAvailableAmount(allProducts);
        allProducts.sort(Comparator.comparing(Product::getName));
        return allProducts;
    }

    private void processAvailableAmount(List<Product> products) {
        products.forEach(this::processAvailableAmount);
    }

    private void processAvailableAmount(Product product) {
        int availableAmount = product.getAvailableAmount() - product.getReservedAmount();
        product.setAvailableAmount(availableAmount);
    }

    @Override
    public void save(Product product) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, product);
        productDAO.save(product);
    }

    @Override
    public void updateById(UUID id, Product product) {
        LOG.debug(LOG_DEBUG_TWO_ARG_PATTERN, product, id);
        productDAO.updateById(id, product);
    }

    @Override
    public void deleteById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        productDAO.deleteById(id);
    }

    @Override
    public Product findById(UUID id) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, id);
        Optional<Product> productOptional = productDAO.findById(id);
        return processInstanceExport(productOptional, id.toString(), Product.ID_FIELD);
    }

    private Product processInstanceExport(Optional<Product> productOptional, String requestedValue, String fieldName) {
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        processGeneralProductException(fieldName, requestedValue);
        return null;
    }

    private void processGeneralProductException(String invalidField, String invalidValue) {
        String message = String.format(GENERAL_PRODUCT_NOT_FOUND_PATTERN, invalidField, invalidValue);
        LOG.warn(message);
        throw new FieldNotFoundException(message);
    }

    @Override
    public void createProduct(Product product) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, product);
        productDAO.saveWithoutId(product);
    }
}
