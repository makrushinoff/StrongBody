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
class CartDAOImplIntegrationTest {

    private static final String EMAIL1 = "cus@bla.com";
    private static final String EMAIL2 = "cus2@bla.com";
    private static final String USERNAME1 = "cutouser";
    private static final String USERNAME2 = "cut2ouser";
    private static final String PASSWORD1 = "cus";
    private static final String PASSWORD2 = "cus2";
    private static final String FIRST_NAME1 = "Custo";
    private static final String FIRST_NAME2 = "Custo2";
    private static final String LAST_NAME1 = "Mer";
    private static final String LAST_NAME2 = "Mer2";
    private static final String PHONE_NUMBER1 = "+99999999";
    private static final String PHONE_NUMBER2 = "+99999999222";
    private static final String SCRIPT_FILENAME = "schema.sql";

    private Customer customer2;
    private Cart cart1;
    private Cart cart2;
    private EmbeddedDatabase database;

    @Autowired
    private CustomerDAOImpl customerDAOImpl;

    @Autowired
    private CartDAOImpl testInstance;

    @BeforeEach
    void setUp() {
        Customer customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        customer1.setEmail(EMAIL1);
        customer1.setUsername(USERNAME1);
        customer1.setPassword(PASSWORD1);
        customer1.setFirstName(FIRST_NAME1);
        customer1.setLastName(LAST_NAME1);
        customer1.setPhoneNumber(PHONE_NUMBER1);
        customerDAOImpl.save(customer1);

        customer2 = new Customer();
        customer2.setId(UUID.randomUUID());
        customer2.setEmail(EMAIL2);
        customer2.setUsername(USERNAME2);
        customer2.setPassword(PASSWORD2);
        customer2.setFirstName(FIRST_NAME2);
        customer2.setLastName(LAST_NAME2);
        customer2.setPhoneNumber(PHONE_NUMBER2);
        customerDAOImpl.save(customer2);

        cart1 = new Cart();
        cart1.setId(UUID.randomUUID());
        cart1.setCustomer(customer1);

        cart2 = new Cart();
        cart2.setId(UUID.randomUUID());
        cart2.setCustomer(customer2);

        database = new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2)
                .addScript(SCRIPT_FILENAME)
                .build();
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void shouldFindAll() {
        testInstance.save(cart1);
        testInstance.save(cart2);

        List<Cart> actual = testInstance.findAll();

        assertThat(actual).contains(cart1).contains(cart2);
    }

    @Test
    void shouldSave() {
        testInstance.save(cart1);

        List<Cart> actual = testInstance.findAll();

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual).contains(cart1);
    }

    @Test
    void shouldUpdateById() {
        testInstance.save(cart1);
        testInstance.save(cart2);

        testInstance.updateById(cart1.getId(), cart2);
        Optional<Cart> actualOptional = testInstance.findById(cart1.getId());

        assertThat(actualOptional).isPresent();
        Cart actual = actualOptional.get();
        assertThat(actual.getId()).isEqualTo(cart1.getId());
        assertThat(actual.getCustomer()).isEqualTo(customer2);
    }

    @Test
    void shouldDeleteById() {
        testInstance.save(cart1);

        testInstance.deleteById(cart1.getId());
        List<Cart> actual = testInstance.findAll();

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldNotDeleteByIdByIrregularId() {
        testInstance.save(cart1);

        testInstance.deleteById(UUID.randomUUID());
        List<Cart> actual = testInstance.findAll();

        assertThat(actual).isNotEmpty().contains(cart1);
    }

    @Test
    void shouldFindById() {
        testInstance.save(cart1);

        Optional<Cart> actualCartOptional = testInstance.findById(cart1.getId());

        assertThat(actualCartOptional).isPresent();
        Cart actualCart = actualCartOptional.get();
        assertThat(actualCart).isEqualTo(cart1);
    }

    @Test
    void shouldFindNotByWrongId() {
        testInstance.save(cart1);

        Optional<Cart> actual = testInstance.findById(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }
}
