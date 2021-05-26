package ua.strongBody.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.dao.GeneralDAO;
import ua.strongBody.models.Cart;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Role;
import ua.strongBody.models.State;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public class CartDAOImpl implements GeneralDAO<Cart> {

    private JdbcTemplate jdbcTemplate;

    public CartDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Cart> findAll() {
        List<Cart> allCarts = jdbcTemplate.query("SELECT cart.* , customer.* FROM cart JOIN customer ON customer.id = cart.customer_id", (rs, rowNum) -> {
            Customer customer = new Customer();
            customer.setId(rs.getObject("customer.id", UUID.class));
            customer.setEmail(rs.getString("customer.email"));
            customer.setFirstName(rs.getString("customer.first_name"));
            customer.setLastName(rs.getString("customer.last_name"));
            customer.setPassword(rs.getString("customer.password"));
            customer.setUsername(rs.getString("customer.username"));
            customer.setPhoneNumber(rs.getString("customer.phone_number"));
            String state = rs.getString("customer.customer_state");
            if (state.equals(State.ACTIVE.toString())) {
                customer.setState(State.ACTIVE);
            } else if (state.equals(State.BANNED.toString())) {
                customer.setState(State.BANNED);
            } else {
                customer.setState(State.DELETED);
            }
            String role = rs.getString("customer.customer_role");
            if (role.equals(Role.ADMIN.toString())) {
                customer.setRole(Role.ADMIN);
            } else {
                customer.setRole(Role.USER);
            }

            Cart cart = new Cart();
            cart.setId(rs.getObject("cart.id", UUID.class));
            cart.setCustomer(customer);

            return cart;
        });
        return allCarts;
    }

    @Override
    public void save(Cart cart) {
        jdbcTemplate.update("INSERT INTO cart VALUES (? , ?)",
                cart.getId(),
                cart.getCustomer().getId());
    }

    @Override
    public void updateById(UUID id, Cart cart) {
        jdbcTemplate.update("UPDATE cart SET " +
                        "customer_id = ? WHERE id = ?",
                cart.getCustomer().getId(),
                id);
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM cart WHERE id = ?", id);
    }

    @Override
    public Optional<Cart> findById(UUID id) {
        List<Cart> allCarts = findAll();
        List<Cart> filteredList = allCarts.stream()
                .filter(cart -> cart.getId().equals(id))
                .collect(Collectors.toList());
        if (filteredList.isEmpty()) {
            return Optional.empty();
        }
        Cart result = filteredList.get(0);
        return Optional.of(result);
    }

}
