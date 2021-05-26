package ua.strongBody.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.strongBody.dao.GeneralDAO;
import ua.strongBody.models.Booking;
import ua.strongBody.models.Product;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BookingDAOImpl implements GeneralDAO<Booking> {

    private JdbcTemplate jdbcTemplate;

    public BookingDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> allBookings = jdbcTemplate.query
                ("SELECT booking.* , product.* FROM booking JOIN product ON product.id = booking.product_id", (rs, rowNum) -> {
                    Booking booking = new Booking();
                    booking.setId(rs.getObject("booking.id", UUID.class));
                    booking.setOrderDate(rs.getObject("booking.order_date", LocalDate.class));
                    booking.setProductAmount(rs.getInt("booking.product_amount"));
                    booking.setOrderNumber(rs.getInt("booking.order_number"));

                    Product product = new Product();
                    product.setId(rs.getObject("product.id", UUID.class));
                    product.setName(rs.getString("product.name"));
                    product.setPrice(rs.getInt("product.price"));
                    product.setArticle(rs.getString("product.article"));
                    product.setDescription(rs.getString("product.description"));
                    product.setAvailableAmount(rs.getInt("product.available_amount"));

                    booking.setProduct(product);

                    return booking;
                });
        return allBookings;
    }

    @Override
    public void save(Booking booking) {
        jdbcTemplate.update("INSERT INTO booking VALUES(?, ?, ?, ?, ?)",
                booking.getId(),
                booking.getOrderDate(),
                booking.getProductAmount(),
                booking.getOrderNumber(),
                booking.getProduct().getId());
    }

    @Override
    public void updateById(UUID id, Booking booking) {
        jdbcTemplate.update("UPDATE booking SET " +
                        "order_date = ?," +
                        "product_amount = ?," +
                        "order_number = ?," +
                        "product_id = ? WHERE id = ?",
                booking.getOrderDate(),
                booking.getProductAmount(),
                booking.getOrderNumber(),
                booking.getProduct().getId(),
                id);
    }

    @Override
    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM booking WHERE id = ?", id);
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        List<Booking> allBooking = findAll();
        List<Booking> filteredList = allBooking.stream()
                .filter(booking -> booking.getId().equals(id))
                .collect(Collectors.toList());
        if (filteredList.isEmpty()) {
            return Optional.empty();
        }
        Booking result = filteredList.get(0);
        return Optional.of(result);
    }

}
