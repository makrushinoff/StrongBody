package ua.com.sportfood.models;


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
    public String toString() {
        return "Customer{" +
                "authorizationData=" + authorizationData +
                '}';
    }
}
