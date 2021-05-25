package ua.strongBody.dao;

import ua.strongBody.models.Product;

import java.util.Optional;

public interface ProductDAO extends GeneralDAO<Product> {
    Optional<Product> findByArticle(String article);
}
