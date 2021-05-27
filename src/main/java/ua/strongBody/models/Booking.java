package ua.strongBody.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Booking implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String ORDER_DATE_FIELD = "order_date";
    public static final String PRODUCT_AMOUNT_FIELD = "product_amount";
    public static final String ORDER_NUMBER_FIELD = "order_number";
    public static final String PRODUCT_ID_FIELD = "product_id";
    public static final String CART_ID_FIELD = "cart_id";

    private UUID id;
    private LocalDate orderDate;
    private int productAmount;
    private String orderNumber;
    private Product product;
    private Cart cart;

    public Booking(UUID id, LocalDate orderDate, int productAmount, String orderNumber, Product product, Cart cart) {
        this.id = id;
        this.orderDate = orderDate;
        this.productAmount = productAmount;
        this.orderNumber = orderNumber;
        this.product = product;
        this.cart = cart;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return productAmount == booking.productAmount && id.equals(booking.id) && Objects.equals(orderDate, booking.orderDate) && Objects.equals(orderNumber, booking.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, productAmount, orderNumber);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Booking.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("orderDate=" + orderDate)
                .add("productAmount=" + productAmount)
                .add("orderNumber=" + orderNumber)
                .add("product_id=" + product.getId())
                .add("cart_id=" + cart.getId())
                .toString();
    }
}
