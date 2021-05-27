package ua.strongBody.populator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.strongBody.models.Customer;
import ua.strongBody.models.CustomerDetails;
import ua.strongBody.models.Role;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerToCustomerDetailsPopulatorTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final Role USER_ROLE = Role.USER;

    private Customer customer;
    private CustomerDetails customerDetails;

    private final CustomerToCustomerDetailsPopulator testInstance = new CustomerToCustomerDetailsPopulator();

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setUsername(USERNAME);
        customer.setPassword(PASSWORD);
        customer.setRole(USER_ROLE);

        customerDetails = new CustomerDetails();
    }

    @Test
    void shouldPopulate() {
        testInstance.convert(customer, customerDetails);

        assertThat(customerDetails.getUsername()).isEqualTo(USERNAME);
        assertThat(customerDetails.getPassword()).isEqualTo(PASSWORD);
        assertThat(customerDetails.isAccountNonExpired()).isTrue();
        assertThat(customerDetails.isCredentialsNonExpired()).isTrue();
        assertThat(customerDetails.isEnabled()).isTrue();
        assertThat(customerDetails.getRole()).isEqualTo(Role.USER);
    }
}
