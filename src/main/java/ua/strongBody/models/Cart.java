package ua.strongBody.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Cart implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String CUSTOMER_ID_FIELD = "customer_id";

    private UUID id;
    private Customer customer;
    private int quantity;
    private BigDecimal price;
    private List<Booking> bookingList;

    public Cart(Customer customer) {
        this.customer = customer;
    }

    public Cart() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id.equals(cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cart.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("customer_id=" + customer.getId())
                .add("quantity=" + quantity)
                .add("price=" + price)
                .toString();
    }
}
