package ua.strongBody.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Product implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String PRICE_FIELD = "price";
    public static final String ARTICLE_FIELD = "article";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String AVAILABLE_AMOUNT_FIELD = "available_amount";
    public static final String RESERVED_AMOUNT_FIELD = "reserved_amount";

    private UUID id;
    private String name;
    private int price;
    private String article;
    private String description;
    private int availableAmount;
    private int reservedAmount;

    public Product(String name, int price, String article, String description, int availableAmount) {
        this.name = name;
        this.price = price;
        this.article = article;
        this.description = description;
        this.availableAmount = availableAmount;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    public int getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(int reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && availableAmount == product.availableAmount && reservedAmount == product.reservedAmount && id.equals(product.id) && name.equals(product.name) && article.equals(product.article) && description.equals(product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, article, description, availableAmount, reservedAmount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("price=" + price)
                .add("article='" + article + "'")
                .add("description='" + description + "'")
                .add("availableAmount=" + availableAmount)
                .add("reservedAmount=" + reservedAmount)
                .toString();
    }
}
