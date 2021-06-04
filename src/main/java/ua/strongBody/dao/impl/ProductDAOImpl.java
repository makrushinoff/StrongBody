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
public class ProductDAOImpl implements ProductDAO {

    private static final String SAVE_QUERY = "INSERT INTO product (id, name, price, article, description, amount)" +
            " VALUES('%s', '%s', '%s', '%s', '%s', '%s')";
    private static final String SAVE_WITHOUT_ID_QUERY = "INSERT INTO product (name, price, article, description, amount)" +
            " VALUES('%s', '%s', '%s', '%s', '%s')";
    private static final String UPDATE_QUERY = "UPDATE product SET " +
            "name = '%s'," +
            "price = '%s'," +
            "article = '%s'," +
            "description = '%s'," +
            "amount = '%s'," +
            "reserved_amount = '%s' WHERE id = '%s'";
    private static final String DELETE_QUERY = "DELETE FROM product WHERE id = '%s'";

    private final JdbcTemplate jdbcTemplate;
    private final ProductAssembly productAssembly;

    public ProductDAOImpl(JdbcTemplate jdbcTemplate, ProductAssembly productAssembly) {
        this.jdbcTemplate = jdbcTemplate;
        this.productAssembly = productAssembly;
    }

    @Override
    public List<Product> findAll() throws DataAccessException {
        return productAssembly.findAllProduct();
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update(String.format(SAVE_QUERY,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getArticle(),
                product.getDescription(),
                product.getAmount())
        );
    }

    @Override
    public void saveWithoutId(Product product) {
        jdbcTemplate.update(String.format(SAVE_WITHOUT_ID_QUERY,
                product.getName(),
                product.getPrice(),
                product.getArticle(),
                product.getDescription(),
                product.getAmount())
        );
    }

    @Override
    public void updateById(UUID id, Product product) {
        jdbcTemplate.update(String.format(UPDATE_QUERY,
                product.getName(),
                product.getPrice(),
                product.getArticle(),
                product.getDescription(),
                product.getAmount(),
                product.getReservedAmount(),
                id)
        );
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update(String.format(DELETE_QUERY, id));
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
