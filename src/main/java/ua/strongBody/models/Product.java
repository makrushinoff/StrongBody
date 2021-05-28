package ua.strongBody.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Product implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String PRICE_FIELD = "price";
    public static final String ARTICLE_FIELD = "article";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String AVAILABLE_AMOUNT_FIELD = "available_amount";

    private UUID id;
    private String name;
    private int price;
    private String article;
    private String description;
    private int availableAmount;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && availableAmount == product.availableAmount && id.equals(product.id) && Objects.equals(name, product.name) && Objects.equals(article, product.article) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, article, description, availableAmount);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", article='" + article + '\'' +
                ", description='" + description + '\'' +
                ", availableAmount=" + availableAmount +
                '}';
    }
}