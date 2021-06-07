package ua.strongBody.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Order implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String ORDER_DATE_FIELD = "order_date";
    public static final String PRODUCT_AMOUNT_FIELD = "product_amount";
    public static final String PRICE_FIELD = "price";
    public static final String CUSTOMER_ID_FIELD = "customer_id";

    private UUID id;
    private LocalDate orderedDate;
    private int productAmount;
    private BigDecimal price;
    private Customer customer;

    public Order() {
    }

    public Order(UUID id, LocalDate orderedDate, int productAmount, BigDecimal price, Customer customer) {
        this.id = id;
        this.orderedDate = orderedDate;
        this.productAmount = productAmount;
        this.price = price;
        this.customer = customer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(LocalDate orderedDate) {
        this.orderedDate = orderedDate;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return productAmount == order.productAmount && Objects.equals(id, order.id) && Objects.equals(orderedDate, order.orderedDate) && Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderedDate, productAmount, price);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("orderedDate=" + orderedDate)
                .add("productAmount=" + productAmount)
                .add("price=" + price)
                .add("customer_id=" + customer.getId())
                .toString();
    }
}
