package ua.strongBody.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.strongBody.assembly.ProductAssembly;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductDAOIml implements ProductDAO {

    private final JdbcTemplate jdbcTemplate;
    private final ProductAssembly productAssembly;

    public ProductDAOIml(JdbcTemplate jdbcTemplate, ProductAssembly productAssembly) {
        this.jdbcTemplate = jdbcTemplate;
        this.productAssembly = productAssembly;
    }

    @Override
    public List<Product> findAll() throws DataAccessException {
        return productAssembly.findAllProduct();
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product (id, name, price, article, description, available_amount) VALUES(?, ?, ?, ?, ?, ?)",
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getArticle(),
                product.getDescription(),
                product.getAvailableAmount());
    }

    @Override
    public void updateById(UUID id, Product product) {
        jdbcTemplate.update("UPDATE product SET " +
                        "name = ?," +
                        "price = ?," +
                        "article = ?," +
                        "description = ?," +
                        "available_amount = ? WHERE id = ?",
                product.getName(),
                product.getPrice(),
                product.getArticle(),
                product.getDescription(),
                product.getAvailableAmount(),
                id);
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM product WHERE id = ?", id);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return findAll().stream()
                .filter(product -> isEqualsById(id, product))
                .findFirst();
    }

    private boolean isEqualsById(UUID id, Product product) {
        return product.getId().equals(id);
    }

    @Override
    public Optional<Product> findByArticle(String article) {
        return findAll().stream()
                .filter(product -> isEqualsByArticle(article, product))
                .findFirst();
    }

    private boolean isEqualsByArticle(String article, Product product) {
        return product.getArticle().equals(article);
    }

}
