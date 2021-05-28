package ua.strongBody.populator;

import org.springframework.stereotype.Component;
import ua.strongBody.models.Customer;
import ua.strongBody.models.CustomerDetails;
import ua.strongBody.models.Role;

@Component
public class CustomerToCustomerDetailsPopulator implements Populator<Customer, CustomerDetails> {

    private static final Role DEFAULT_ROLE = Role.USER;

    @Override
    public void convert(Customer customer, CustomerDetails customerDetails) {
        customerDetails.setUsername(customer.getUsername());
        customerDetails.setPassword(customer.getPassword());
        customerDetails.setAccountNonExpired(true);
        customerDetails.setAccountNonLocked(true);
        customerDetails.setCredentialsNonExpired(true);
        customerDetails.setEnabled(true);
        customerDetails.setRole(DEFAULT_ROLE);
    }
}
