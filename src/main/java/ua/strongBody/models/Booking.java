package ua.strongBody.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Booking implements Serializable {
    private UUID id;
    private LocalDate orderDate;
    private int productAmount;
    private int orderNumber;
    private Product product;

    public Booking(LocalDate orderDate, int productAmount, int orderNumber, Product product) {
        this.orderDate = orderDate;
        this.productAmount = productAmount;
        this.orderNumber = orderNumber;
        this.product = product;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return productAmount == booking.productAmount && orderNumber == booking.orderNumber && Objects.equals(id, booking.id) && Objects.equals(orderDate, booking.orderDate) && Objects.equals(product, booking.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, productAmount, orderNumber, product);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", productAmount=" + productAmount +
                ", orderNumber=" + orderNumber +
                ", product=" + product +
                '}';
    }

}
