package ua.com.sportfood.models;

import java.time.LocalDate;
import java.util.UUID;

public class Booking {
    private UUID id;
    private LocalDate date;
    private int productAmount;
    private int orderNumber;
    private Product product;
    private UUID cartId;

    public Booking(LocalDate date, int productAmount, int orderNumber, Product product, UUID cartId) {
        this.date = date;
        this.productAmount = productAmount;
        this.orderNumber = orderNumber;
        this.product = product;
        this.cartId = cartId;
    }

    public Booking() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "date=" + date +
                ", productAmount=" + productAmount +
                ", orderNumber=" + orderNumber +
                ", product=" + product +
                ", cartId=" + cartId +
                '}';
    }
}
