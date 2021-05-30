package ua.strongBody.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.exceptions.IdNotFoundException;
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
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        processIdNotFoundException(id);
        return null;
    }

    private void processIdNotFoundException(UUID id) {
        String message = String.format(GENERAL_BOOKING_NOT_FOUND_PATTERN, Product.ID_FIELD, id);
        LOG.warn(message);
        throw new IdNotFoundException(message);
    }

    @Override
    public void createProduct(Product product) {
        LOG.debug(LOG_DEBUG_ONE_ARG_PATTERN, product);
        productDAO.saveWithoutId(product);
    }
}
