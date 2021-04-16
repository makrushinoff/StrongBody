package ua.com.sportfood.models;

import java.time.LocalDate;
import java.util.UUID;

public class Booking {
    private UUID id;
    private LocalDate orderDate;
    private int productAmount;
    private int orderNumber;
    private Product product;
    private UUID cartId;

    public Booking(LocalDate orderDate, int productAmount, int orderNumber, Product product, UUID cartId) {
        this.orderDate = orderDate;
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

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
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
                "date=" + orderDate +
                ", productAmount=" + productAmount +
                ", orderNumber=" + orderNumber +
                ", product=" + product +
                ", cartId=" + cartId +
                '}';
    }
}
