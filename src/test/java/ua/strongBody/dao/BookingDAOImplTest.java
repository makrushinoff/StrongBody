package ua.strongBody.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ua.strongBody.dao.impl.BookingDAOImpl;
import ua.strongBody.models.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ComponentScan(basePackages = "ua.strongBody.dao")
public class BookingDAOImplTest {

    @Autowired
    private BookingDAOImpl bookingDAO;
    @Autowired
    private CustomerDAO customerDAO;
    @Autowired
    private ProductDAO productDAO;
    private Booking booking1;
    private Booking booking2;
    private EmbeddedDatabase database;
    private LocalDate local;
    private Product product1;
    private Customer customer1;


    @BeforeEach
    void setUp() {
        database = new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        local = LocalDate.of(2021, 10, 28);
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());
        productDAO.save(product1);
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"

        );
        customer1.setId(UUID.randomUUID());
        customer1.setRole(Role.USER);
        customer1.setState(State.ACTIVE);
        customerDAO.save(customer1);

        booking1 = new Booking(
                local,
                10,
                1234,
                product1);
        booking1.setId(UUID.randomUUID());

        booking2 = new Booking(
                local,
                15,
                4321,
                product1);
        booking2.setId(UUID.randomUUID());

    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void shouldFindAll() {
        bookingDAO.save(booking1);

        Booking unexpected = new Booking();
        unexpected.setId(UUID.randomUUID());

        List<Booking> result = bookingDAO.findAll();

        assertThat(result.contains(booking1)).isTrue();

    }

    @Test
    void shouldSave() {
        bookingDAO.save(booking1);
        List<Booking> result = bookingDAO.findAll();
        assertThat(result.size() == 1).isTrue();

    }

    @Test
    void shouldUpdateById() {
        bookingDAO.save(booking1);

        bookingDAO.save(booking2);
        bookingDAO.updateById(booking1.getId() , booking2);

    }

    @Test
    void shouldNotFindBookingAfterUpdate() {
        booking1.setId(booking2.getId());
        bookingDAO.save(booking1);
        bookingDAO.updateById(booking1.getId() , booking2);

        List<Booking> actualList = bookingDAO.findAll();
        Booking actual = actualList.get(0);

        assertThat(actual).isNotEqualTo(booking1);
    }

    @Test
    void shouldDeleteById() {
        bookingDAO.save(booking1);
        List<Booking> resultListAfterSave = bookingDAO.findAll();
        int resultSizeAfterSave = resultListAfterSave.size();
        bookingDAO.deleteById(booking1.getId());
        List<Booking> resultListAfterDelete = bookingDAO.findAll();
        int resultSizeAfterDelete = resultListAfterDelete.size();
        int actual = resultSizeAfterSave - resultSizeAfterDelete;

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void shouldNotDeleteByIdByIrregularId() {
        bookingDAO.save(booking1);
        List<Booking> resultListAfterSave = bookingDAO.findAll();
        int resultSizeAfterSave = resultListAfterSave.size();
        bookingDAO.deleteById(UUID.randomUUID());
        List<Booking> resultListAfterDelete = bookingDAO.findAll();
        int resultSizeAfterDelete = resultListAfterDelete.size();
        int actual = resultSizeAfterSave - resultSizeAfterDelete;

        assertThat(actual).isZero();
    }

    @Test
    void shouldFindById() {
        bookingDAO.save(booking1);

        Optional<Booking> result = bookingDAO.findById(booking1.getId());
        assertThat(result).isPresent();
        Booking actualBooking = result.get();
        assertThat(actualBooking).isEqualTo(booking1);
    }

    @Test
    void shouldFindNotByWrongId() {
        bookingDAO.save(booking1);
        Optional<Booking> actual = bookingDAO.findById(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }

}
