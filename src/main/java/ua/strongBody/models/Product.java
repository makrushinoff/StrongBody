package ua.strongBody.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Product implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String PRICE_FIELD = "price";
    public static final String ARTICLE_FIELD = "article";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String AMOUNT_FIELD = "amount";
    public static final String RESERVED_AMOUNT_FIELD = "reserved_amount";

    private UUID id;
    private String name;
    private BigDecimal price;
    private String article;
    private String description;
    private int amount;
    private int reservedAmount;
    private int availableAmount;

    public Product(String name, BigDecimal price, String article, String description, int amount) {
        this.name = name;
        this.price = price;
        this.article = article;
        this.description = description;
        this.amount = amount;
    }

    public Product() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(int reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price.equals(product.price) && amount == product.amount && reservedAmount == product.reservedAmount && id.equals(product.id) && name.equals(product.name) && article.equals(product.article) && description.equals(product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, article, description, amount, reservedAmount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("price=" + price)
                .add("article='" + article + "'")
                .add("description='" + description + "'")
                .add("amount=" + amount)
                .add("reservedAmount=" + reservedAmount)
                .add("availableAmount=" + availableAmount)
                .toString();
    }
}
