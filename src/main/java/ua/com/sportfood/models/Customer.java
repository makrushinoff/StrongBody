package ua.com.sportfood.models;


import java.util.Objects;
import java.util.UUID;

public class Customer {
    private UUID id;
    private AuthorizationData authorizationData;

    public Customer(String email, String username,
                    String password, String firstName, String lastName, String phoneNumber) {
        authorizationData = new AuthorizationData(email,username,password,firstName,lastName,phoneNumber);
        authorizationData.setId(UUID.randomUUID());
    }

    public Customer() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AuthorizationData getAuthorizationData() {
        return authorizationData;
    }

    public void setAuthorizationData(AuthorizationData authorizationData) {
        this.authorizationData = authorizationData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(authorizationData, customer.authorizationData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationData);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", authorizationData=" + authorizationData +
                '}';
    }
}
