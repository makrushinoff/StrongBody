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
import ua.strongBody.models.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ComponentScan(basePackages = "ua.strongBody.dao")
class CustomerDAOImplIntegrationTest {

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

    private Customer customer1;
    private Customer customer2;
    private EmbeddedDatabase database;

    @Autowired
    private CustomerDAO testInstance;

    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setId(UUID.randomUUID());
        customer1.setEmail(EMAIL1);
        customer1.setUsername(USERNAME1);
        customer1.setPassword(PASSWORD1);
        customer1.setFirstName(FIRST_NAME1);
        customer1.setLastName(LAST_NAME1);
        customer1.setPhoneNumber(PHONE_NUMBER1);

        customer2 = new Customer();
        customer2.setEmail(EMAIL2);
        customer2.setUsername(USERNAME2);
        customer2.setPassword(PASSWORD2);
        customer2.setFirstName(FIRST_NAME2);
        customer2.setLastName(LAST_NAME2);
        customer2.setPhoneNumber(PHONE_NUMBER2);

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
        customer2.setId(UUID.randomUUID());
        testInstance.save(customer1);
        testInstance.save(customer2);

        List<Customer> actual = testInstance.findAll();

        assertThat(actual).contains(customer1).contains(customer2);
    }

    @Test
    void shouldNotContainsCustomer() {
        customer2.setId(UUID.randomUUID());
        testInstance.save(customer1);

        List<Customer> actual = testInstance.findAll();

        assertThat(actual.contains(customer2)).isFalse();
    }

    @Test
    void shouldUpdateById() {
        customer2.setId(customer1.getId());
        testInstance.save(customer1);

        testInstance.updateById(customer1.getId(), customer2);
        List<Customer> actual = testInstance.findAll();

        assertThat(actual).contains(customer2);
    }

    @Test
    void shouldCustomerNotBeEqualsAfterUpdate() {
        customer2.setId(customer1.getId());
        testInstance.save(customer1);

        testInstance.updateById(customer1.getId(), customer2);
        List<Customer> actualList = testInstance.findAll();

        Customer actualCustomer = actualList.get(0);
        assertThat(actualCustomer).isNotEqualTo(customer1);
    }

    @Test
    void shouldDeleteById() {
        testInstance.save(customer1);

        testInstance.deleteById(customer1.getId());
        List<Customer> actual = testInstance.findAll();

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldFindById() {
        testInstance.save(customer1);

        Optional<Customer> actualCustomerOptional = testInstance.findById(customer1.getId());

        assertThat(actualCustomerOptional).isPresent();
        Customer actualCustomer = actualCustomerOptional.get();
        assertThat(actualCustomer).isEqualTo(customer1);
    }

    @Test
    void shouldFindByUsername() {
        testInstance.save(customer1);

        Optional<Customer> actualOptional = testInstance.findFirstByUsername(USERNAME1);

        assertThat(actualOptional).isPresent();
        Customer actual = actualOptional.get();
        assertThat(actual).isEqualTo(customer1);
    }
}
