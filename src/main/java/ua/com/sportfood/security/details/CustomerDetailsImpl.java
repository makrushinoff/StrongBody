package ua.com.sportfood.security.details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.com.sportfood.models.Customer;
import ua.com.sportfood.models.State;

import java.util.Collection;
import java.util.Collections;

public class CustomerDetailsImpl implements UserDetails {

    private Customer customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = customer.getRole().name();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return Collections.singletonList(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !customer.getState().equals(State.BANNED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customer.getState().equals(State.ACTIVE);
    }
}
