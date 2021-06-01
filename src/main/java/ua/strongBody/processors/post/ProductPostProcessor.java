package ua.strongBody.processors.post;

import org.springframework.stereotype.Component;
import ua.strongBody.models.Product;

import java.util.Comparator;
import java.util.List;

@Component
public class ProductPostProcessor implements PostProcessor<Product> {
    @Override
    public void postProcess(List<Product> products) {
        sortProducts(products);
        products.forEach(this::postProcess);
    }

    @Override
    public void postProcess(Product product) {
        calculateAvailableAmount(product);
    }

    private void sortProducts(List<Product> products) {
        products.sort(Comparator.comparing(Product::getName));
    }

    private void calculateAvailableAmount(Product product) {
        int availableAmount = product.getAmount() - product.getReservedAmount();
        product.setAvailableAmount(availableAmount);
    }
}
