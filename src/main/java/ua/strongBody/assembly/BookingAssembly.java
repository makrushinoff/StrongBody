package ua.strongBody.assembly;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.strongBody.models.Booking;

import java.util.List;

@Component
public class BookingAssembly {

    private static final String FIND_ALL_QUERY = "SELECT * FROM booking";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Booking> bookingRowMapper;

    public BookingAssembly(JdbcTemplate jdbcTemplate, RowMapper<Booking> bookingRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookingRowMapper = bookingRowMapper;
    }

    public List<Booking> findAllSingleLayer() throws DataAccessException {
        return jdbcTemplate.query(FIND_ALL_QUERY, bookingRowMapper);
    }
}
