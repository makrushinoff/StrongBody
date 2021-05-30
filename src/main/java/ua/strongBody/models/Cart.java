package ua.strongBody.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Cart implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String CUSTOMER_ID_FIELD = "customer_id";

    private UUID id;
    private Customer customer;

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
                .toString();
    }
}
