package ua.com.sportfood.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.sportfood.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProductDAO implements GeneralDAO<Product> {
    private JdbcTemplate jdbcTemplate;

    public ProductDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        List<Product> allProducts = jdbcTemplate.query("SELECT * FROM product", (resultSet, i) -> parseResultSetToProduct(resultSet));
        return allProducts;
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product VALUES(?, ?, ?, ?, ?, ?)",
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
    public Product findById(UUID id) {
        List<Product> allProducts = findAll();
        List<Product> filteredList = allProducts.stream()
                .filter(product -> product.getId().equals(id))
                .collect(Collectors.toList());
        if(filteredList.size() > 1) try {
            throw new SQLException();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return filteredList.get(0);
    }

    public Product findByArticle(String article) {
        List<Product> allProducts = findAll();
        List<Product> filteredList = allProducts.stream()
                .filter(product -> product.getArticle().equals(article)).collect(Collectors.toList());
        if(filteredList.size() > 1) try {
            throw new SQLException();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return filteredList.get(0);
    }

    private Product parseResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getObject("id", UUID.class));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getInt("price"));
        product.setArticle(resultSet.getString("article"));
        product.setDescription(resultSet.getString("description"));
        product.setAvailableAmount(resultSet.getInt("available_amount"));
        return product;
    }
}
