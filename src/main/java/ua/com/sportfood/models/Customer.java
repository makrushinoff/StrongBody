package ua.com.sportfood.models;


import java.util.UUID;

public class Customer {
    private UUID id;
    private AuthorizationData authorizationData;

    public Customer(AuthorizationData authorizationData) {
        this.authorizationData = authorizationData;
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
    public String toString() {
        return "Customer{" +
                "authorizationData=" + authorizationData +
                '}';
    }
}
