package ua.strongBody.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static ua.strongBody.models.Product.*;

@Component
public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getObject(ID_FIELD, UUID.class));
        product.setName(resultSet.getString(NAME_FIELD));
        product.setPrice(resultSet.getBigDecimal(PRICE_FIELD));
        product.setArticle(resultSet.getString(ARTICLE_FIELD));
        product.setDescription(resultSet.getString(DESCRIPTION_FIELD));
        product.setAmount(resultSet.getInt(AMOUNT_FIELD));
        product.setReservedAmount(resultSet.getInt(RESERVED_AMOUNT_FIELD));

        return product;
    }
}
