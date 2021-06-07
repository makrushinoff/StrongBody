package ua.strongBody.models.forms;

import ua.strongBody.models.Product;

import java.util.StringJoiner;

public class RegistrationForm {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;

    public static final String USERNAME_FIELD = "username";
    public static final String EMAIL_FIELD = "email";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";

    public RegistrationForm() {
    }

    public RegistrationForm(String firstName, String lastName, String username, String password, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RegistrationForm.class.getSimpleName() + "[", "]")
                .add("firstName=" + firstName)
                .add("lastName='" + lastName + "'")
                .add("username=" + username)
                .add("password='" + password + "'")
                .add("email='" + email + "'")
                .add("phoneNumber=" + phoneNumber)
                .toString();
    }
}
