package ua.com.sportfood.populator;

import org.springframework.stereotype.Component;
import ua.com.sportfood.models.Customer;
import ua.com.sportfood.models.CustomerDetails;

@Component
public class CustomerToCustomerDetailsPopulator implements Populator<Customer, CustomerDetails> {

    @Override
    public void populate(Customer customer, CustomerDetails customerDetails) {
        customerDetails.setUsername(customer.getUsername());
        customerDetails.setPassword(customer.getPassword());
        customerDetails.setAccountNonExpired(true);
        customerDetails.setCredentialsNonExpired(true);
        customerDetails.setEnabled(true);
        customerDetails.setRole(customer.getRole());
    }
}
