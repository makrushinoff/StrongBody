package ua.strongBody.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ua.strongBody.dao.CartDAO;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.dao.ProductDAO;
import ua.strongBody.dao.impl.BookingDAOImpl;
import ua.strongBody.models.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ComponentScan(basePackages = "ua.strongBody.dao")
class BookingDAOImplIntegrationTest {

    private static final LocalDate ORDER_DATE1 = LocalDate.of(2021, 10, 28);
    private static final LocalDate ORDER_DATE2 = LocalDate.of(2021, 10, 20);
    private static final int PRODUCT_AMOUNT1 = 10;
    private static final int PRODUCT_AMOUNT2 = 20;
    private static final String ORDER_NUMBER1 = "12345";
    private static final String ORDER_NUMBER2 = "12346";
    private static final String PRODUCT_NAME = "prod1";
    private static final int PRICE = 2000;
    private static final String ARTICLE = "PP-01";
    private static final String DESCRIPTION = "Super-puper product1";
    private static final int AVAILABLE_AMOUNT = 5;
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

    private Booking booking1;
    private Booking booking2;
    private EmbeddedDatabase database;


    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private BookingDAOImpl testInstance;

    @BeforeEach
    void setUp() {
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName(PRODUCT_NAME);
        product1.setPrice(PRICE);
        product1.setArticle(ARTICLE);
        product1.setDescription(DESCRIPTION);
        product1.setAmount(AVAILABLE_AMOUNT);
        productDAO.save(product1);

        Customer customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        customer1.setEmail(EMAIL1);
        customer1.setUsername(USERNAME1);
        customer1.setPassword(PASSWORD1);
        customer1.setFirstName(FIRST_NAME1);
        customer1.setLastName(LAST_NAME1);
        customer1.setPhoneNumber(PHONE_NUMBER1);
        customerDAO.save(customer1);

        Customer customer2 = new Customer();
        customer2.setId(UUID.randomUUID());
        customer2.setEmail(EMAIL2);
        customer2.setUsername(USERNAME2);
        customer2.setPassword(PASSWORD2);
        customer2.setFirstName(FIRST_NAME2);
        customer2.setLastName(LAST_NAME2);
        customer2.setPhoneNumber(PHONE_NUMBER2);
        customerDAO.save(customer2);

        Cart cart1 = new Cart();
        cart1.setId(UUID.randomUUID());
        cart1.setCustomer(customer1);
        cartDAO.save(cart1);

        Cart cart2 = new Cart();
        cart2.setId(UUID.randomUUID());
        cart2.setCustomer(customer2);
        cartDAO.save(cart2);

        booking1 = new Booking();
        booking1.setId(UUID.randomUUID());
        booking1.setOrderDate(ORDER_DATE1);
        booking1.setProductAmount(PRODUCT_AMOUNT1);
        booking1.setOrderNumber(ORDER_NUMBER1);
        booking1.setProduct(product1);
        booking1.setCart(cart1);

        booking2 = new Booking();
        booking2.setId(UUID.randomUUID());
        booking2.setOrderDate(ORDER_DATE2);
        booking2.setProductAmount(PRODUCT_AMOUNT2);
        booking2.setOrderNumber(ORDER_NUMBER2);
        booking2.setProduct(product1);
        booking2.setCart(cart2);

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
        testInstance.save(booking1);

        Booking unexpected = new Booking();
        unexpected.setId(UUID.randomUUID());

        List<Booking> actual = testInstance.findAll();

        assertThat(actual).contains(booking1);
    }

    @Test
    void shouldSave() {
        testInstance.save(booking1);

        List<Booking> actual = testInstance.findAll();

        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void shouldUpdateById() {
        testInstance.save(booking1);
        testInstance.save(booking2);

        testInstance.updateById(booking1.getId(), booking2);
        Optional<Booking> actualOptional = testInstance.findById(booking1.getId());

        assertThat(actualOptional).isPresent();
        Booking actual = actualOptional.get();

        assertThat(actual.getId()).isEqualTo(booking1.getId());
        actual.setId(booking2.getId());
        assertThat(actual).isEqualTo(booking2);
    }

    @Test
    void shouldNotFindBookingAfterUpdate() {
        booking1.setId(booking2.getId());
        testInstance.save(booking1);

        testInstance.updateById(booking1.getId(), booking2);
        List<Booking> actualList = testInstance.findAll();
        Booking actual = actualList.get(0);

        assertThat(actual).isNotEqualTo(booking1);
    }

    @Test
    void shouldDeleteById() {
        testInstance.save(booking1);
        testInstance.save(booking2);

        testInstance.deleteById(booking1.getId());
        List<Booking> actual = testInstance.findAll();

        assertThat(actual).isNotEmpty().contains(booking2);
        assertThat(actual.contains(booking1)).isFalse();
    }

    @Test
    void shouldNotDeleteByIdByIrregularId() {
        testInstance.save(booking1);
        testInstance.save(booking2);

        testInstance.deleteById(UUID.randomUUID());
        List<Booking> actual = testInstance.findAll();

        assertThat(actual).isNotEmpty().contains(booking1).contains(booking2);
    }

    @Test
    void shouldFindById() {
        testInstance.save(booking1);

        Optional<Booking> actual = testInstance.findById(booking1.getId());

        assertThat(actual).isPresent();
        Booking actualBooking = actual.get();
        assertThat(actualBooking).isEqualTo(booking1);
    }

    @Test
    void shouldFindNotByWrongId() {
        testInstance.save(booking1);

        Optional<Booking> actual = testInstance.findById(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }
}
