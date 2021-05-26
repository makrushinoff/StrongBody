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
import ua.strongBody.dao.impl.CartDAOImpl;
import ua.strongBody.dao.impl.CustomerDAOImpl;
import ua.strongBody.models.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ComponentScan(basePackages = "ua.strongBody.dao")
public class CartDAOImplTest {

    @Autowired
    private CartDAOImpl cartDAOImpl;
    @Autowired
    private CustomerDAOImpl customerDAOImpl;
    private EmbeddedDatabase database;
    private Cart cart;
    private Cart cart2;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        database = new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();

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
        customerDAOImpl.save(customer1);

        customer2 = new Customer(
                "fbfn@bla.com",
                "user",
                "cusdvd",
                "Custo12",
                "Mer",
                "+99119999"

        );
        customer2.setId(UUID.randomUUID());
        customer2.setRole(Role.USER);
        customer2.setState(State.ACTIVE);
        customerDAOImpl.save(customer2);

        cart = new Cart(customer1);
        cart.setId(UUID.randomUUID());

        cart2 = new Cart(customer2);
        cart2.setId(UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void shouldFindAll() {
        cartDAOImpl.save(cart);

        Cart unexpected = new Cart();
        unexpected.setId(UUID.randomUUID());
        List<Cart> result = cartDAOImpl.findAll();

        assertThat(result.contains(cart)).isTrue();
    }

    @Test
    void shouldSave() {
        cartDAOImpl.save(cart);

        List<Cart> result = cartDAOImpl.findAll();
        assertThat(result.size() == 1).isTrue();
    }

    @Test
    void shouldUpdateById() {
        cartDAOImpl.save(cart);

        cartDAOImpl.save(cart2);
        cartDAOImpl.updateById(cart.getId(),cart2);
    }

    @Test
    void shouldNotFindBookingAfterUpdate() {
        cart.setId(cart2.getId());
        cartDAOImpl.save(cart);
        cartDAOImpl.updateById(cart.getId(),cart2);

        List<Cart> actualList = cartDAOImpl.findAll();
        Cart actual = actualList.get(0);

        assertThat(actual).isNotEqualTo(cart);
    }

    @Test
    void shouldDeleteById() {
        cartDAOImpl.save(cart);
        List<Cart> resultListAfterSave = cartDAOImpl.findAll();
        int resultSizeAfterSave = resultListAfterSave.size();
        cartDAOImpl.deleteById(cart.getId());
        List<Cart> resultListAfterDelete = cartDAOImpl.findAll();
        int resultSizeAfterDelete = resultListAfterDelete.size();
        int actual = resultSizeAfterSave - resultSizeAfterDelete;

        assertThat(actual).isEqualTo(1);
    }

    @Test
    void shouldNotDeleteByIdByIrregularId() {
        cartDAOImpl.save(cart);
        List<Cart> resultListAfterSave = cartDAOImpl.findAll();
        int resultSizeAfterSave = resultListAfterSave.size();
        cartDAOImpl.deleteById(UUID.randomUUID());
        List<Cart> resultListAfterDelete = cartDAOImpl.findAll();
        int resultSizeAfterDelete = resultListAfterDelete.size();
        int actual = resultSizeAfterSave - resultSizeAfterDelete;

        assertThat(actual).isZero();
    }

    @Test
    void shouldFindById() {
        cartDAOImpl.save(cart);
        Optional<Cart> result = cartDAOImpl.findById(cart.getId());
        assertThat(result).isPresent();
        Cart actualCart = result.get();
        assertThat(actualCart).isEqualTo(cart);
    }

    @Test
    void shouldFindNotByWrongId() {
        cartDAOImpl.save(cart);
        Optional<Cart> actual = cartDAOImpl.findById(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }
}
