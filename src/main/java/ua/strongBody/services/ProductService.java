package ua.strongBody.services;

import ua.strongBody.models.Product;

public interface ProductService extends GeneralService<Product> {

    void createProduct(Product product);
}
