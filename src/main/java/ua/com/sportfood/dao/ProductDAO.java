package ua.com.sportfood.dao;

import ua.com.sportfood.models.Product;

import java.util.Optional;

public interface ProductDAO extends GeneralDAO<Product> {
    Optional<Product> findByArticle(String article);
}
