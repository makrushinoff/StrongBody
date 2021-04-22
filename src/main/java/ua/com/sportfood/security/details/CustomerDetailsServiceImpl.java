package ua.com.sportfood.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.sportfood.dao.CustomerDAO;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user with username %s not found";
    @Autowired
    private  CustomerDAO customerDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) customerDAO.findByUsername(username);
    }
}
