package ua.strongBody.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.exceptions.FieldValidationException;
import ua.strongBody.exceptions.ValidationException;
import ua.strongBody.models.Product;

import java.util.List;

import static ua.strongBody.constants.LoggingConstants.LOG_DEBUG_ONE_ARG_PATTERN;
import static ua.strongBody.models.Product.ARTICLE_FIELD;
import static ua.strongBody.models.Product.NAME_FIELD;

@Service
public class ProductValidator implements Validator<Product> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductValidator.class);

    private static final String VALIDATION_EXCEPTION_PATTERN = "Field: '%s', value: '%s' , reason: '%s'";
    private static final String DUPLICATION_REASON = "Duplicated instance in database";

    private final ProductDAO productDAO;

    public ProductValidator(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public void validate(Product product) throws ValidationException {
        LOG.info(LOG_DEBUG_ONE_ARG_PATTERN, product);
        List<Product> allProducts = productDAO.findAll();
        try {
            allProducts.forEach(prod -> validateFields(product, prod));
        } catch (FieldValidationException ex) {
            String message = generateValidationMessage(ex);
            LOG.warn(message);
            throw new ValidationException(message);
        }
    }

    private String generateValidationMessage(FieldValidationException ex) {
        String invalidFieldName = ex.getInvalidFieldName();
        String invalidValue = ex.getInvalidValue();
        String reason = ex.getReason();
        return String.format(VALIDATION_EXCEPTION_PATTERN, invalidFieldName, invalidValue, reason);
    }

    private void validateFields(Product inputProduct, Product presenceProduct)  throws FieldValidationException {
        articleValidation(inputProduct, presenceProduct);
        nameValidation(inputProduct, presenceProduct);
    }

    private void nameValidation(Product inputProduct, Product presenceProduct) {
        boolean nameCheck = presenceProduct.getName().equals(inputProduct.getName());
        if(nameCheck) {
            throw new FieldValidationException(NAME_FIELD, inputProduct.getName(), DUPLICATION_REASON);
        }
    }

    private void articleValidation(Product inputProduct, Product presenceProduct) {
        boolean articleCheck = presenceProduct.getArticle().equals(inputProduct.getArticle());
        if(articleCheck) {
            throw new FieldValidationException(ARTICLE_FIELD, inputProduct.getArticle(), DUPLICATION_REASON);
        }
    }
}
