package ua.com.sportfood.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ua.com.sportfood.models.Customer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ComponentScan(basePackages = "ua.com.sportfood.dao")
class CustomerDAOTest {

    @Autowired
    private CustomerDAO customerDAO;
    private Customer customer1;
    private Customer customer2;
    private EmbeddedDatabase database;

    @BeforeEach
    void setUp() {
        database = new EmbeddedDatabaseBuilder().
                 setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();

    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void shouldFindAll() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customer2 = new Customer(
                "cus2@bla.com",
                "cut2ouser",
                "cus2",
                "Custo2",
                "Mer2",
                "+99999999222"
        );
        customer2.setId(UUID.randomUUID());
        customerDAO.save(customer1);
        customerDAO.save(customer2);
        List<Customer> result = customerDAO.findAll();
        assertThat(result.contains(customer1)).isTrue();
        assertThat(result.contains(customer2)).isTrue();
    }

    @Test
    void shouldNotContainsCustomer() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customerDAO.save(customer1);
        customer2 = new Customer(
                "cus2@bla.com",
                "cut2ouser",
                "cus2",
                "Custo2",
                "Mer2",
                "+99999999222"
        );
        customer2.setId(UUID.randomUUID());

        List<Customer> result = customerDAO.findAll();

        assertThat(result.contains(customer2)).isFalse();
    }

    @Test
    void shouldSave() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customer2 = new Customer(
                "cus2@bla.com",
                "cut2ouser",
                "cus2",
                "Custo2",
                "Mer2",
                "+99999999222"
        );

        customer2.setId(UUID.randomUUID());

        customerDAO.save(customer1);
        customerDAO.save(customer2);
    }

    @Test
    void shouldUpdateById() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customerDAO.save(customer1);
        customer2 = new Customer(
                "cus2@bla.com",
                "cut2ouser",
                "cus2",
                "Custo2",
                "Mer2",
                "+99999999222"
        );
        customer2.setId(customer1.getId());
        customer2.getAuthorizationData().setId(customer1.getAuthorizationData().getId());
        customerDAO.updateById(customer1.getId(), customer2);

        List<Customer> resultList = customerDAO.findAll();
        Customer resultCustomer = resultList.get(0);

        assertThat(resultCustomer.equals(customer2)).isTrue();

    }

    @Test
    void shouldCustomerNotBeEqualsAfterUpdate() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customerDAO.save(customer1);
        customer2 = new Customer(
                "cus2@bla.com",
                "cut2ouser",
                "cus2",
                "Custo2",
                "Mer2",
                "+99999999222"
        );
        customer2.setId(customer1.getId());
        customer2.getAuthorizationData().setId(customer1.getAuthorizationData().getId());
        customerDAO.updateById(customer1.getId(), customer2);

        List<Customer> resultList = customerDAO.findAll();
        Customer resultCustomer = resultList.get(0);

        assertThat(resultCustomer.equals(customer1)).isFalse();
    }

    @Test
    void shouldDeleteById() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customerDAO.save(customer1);

        customerDAO.deleteById(customer1.getId());

        List<Customer> all = customerDAO.findAll();

        assertThat(all.size() == 0).isTrue();
    }

    @Test
    void shouldFindById() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customerDAO.save(customer1);

        Customer result = customerDAO.findById(customer1.getId());

        assertThat(result.equals(customer1)).isTrue();
    }

    @Test
    void shouldFindByUsername() {
        customer1 = new Customer(
                "cus@bla.com",
                "cutouser",
                "cus",
                "Custo",
                "Mer",
                "+99999999"
        );
        customer1.setId(UUID.randomUUID());
        customerDAO.save(customer1);

        Customer result = customerDAO.findByUsername(customer1.getAuthorizationData().getUsername());

        assertThat(result.equals(customer1)).isTrue();
    }
}