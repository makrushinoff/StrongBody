package ua.com.sportfood.dao;

import org.assertj.core.internal.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ua.com.sportfood.models.Product;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@ComponentScan(basePackages = "ua.com.sportfood.dao")
class ProductDAOTest {

    @Autowired
    private ProductDAO productDAO;
    private Product product1;
    private Product product2;
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
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());

        product2 = new Product(
                "prod2",
                3000,
                "PP-02",
                "Wruper-hruper product2",
                7
        );
        product2.setId(UUID.randomUUID());

        productDAO.save(product1);
        productDAO.save(product2);
        Product unexpected = new Product();
        unexpected.setId(UUID.randomUUID());

        List<Product> result = productDAO.findAll();

        assertThat(result.contains(product1)).isTrue();
        assertThat(result.contains(product2)).isTrue();
    }

    @Test
    void shouldNotFindProduct() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());

        product2 = new Product(
                "prod2",
                3000,
                "PP-02",
                "Wruper-hruper product2",
                7
        );
        product2.setId(UUID.randomUUID());

        productDAO.save(product1);
        productDAO.save(product2);
        Product unexpected = new Product();
        unexpected.setId(UUID.randomUUID());

        List<Product> result = productDAO.findAll();

        assertThat(result.contains(unexpected)).isFalse();
    }

    @Test
    void shouldSave() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());


        productDAO.save(product1);
        List<Product> result = productDAO.findAll();
        assertThat(result.size() == 1).isTrue();
    }

    @Test
    void shouldUpdateById() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());

        product2 = new Product(
                "prod2",
                3000,
                "PP-02",
                "Wruper-hruper product2",
                7
        );
        product2.setId(product1.getId());

        productDAO.save(product1);

        productDAO.updateById(product1.getId(), product2);
        List<Product> all = productDAO.findAll();
        assertThat(all.get(0).equals(product2)).isTrue();
    }

    @Test
    void shouldNotFindProductAfterUpdate() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());

        product2 = new Product(
                "prod2",
                3000,
                "PP-02",
                "Wruper-hruper product2",
                7
        );
        product2.setId(product1.getId());

        productDAO.save(product1);

        productDAO.updateById(product1.getId(), product2);

        List<Product> all = productDAO.findAll();
        assertThat(all.get(0).equals(product1)).isFalse();
    }

    @Test
    void shouldDeleteById() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());
        productDAO.save(product1);

        List<Product> resultListAfterSave = productDAO.findAll();
        int resultAfterSave = resultListAfterSave.size();
        productDAO.deleteById(product1.getId());
        List<Product> resultListAfterDelete = productDAO.findAll();
        int resultAfterDelete = resultListAfterDelete.size();

        assertThat(resultAfterSave - resultAfterDelete == 1).isTrue();
    }

    @Test
    void shouldNotDeleteByIdByIrregularId() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());
        productDAO.save(product1);

        List<Product> resultListAfterSave = productDAO.findAll();
        int resultAfterSave = resultListAfterSave.size();
        productDAO.deleteById(UUID.randomUUID());
        List<Product> resultListAfterDelete = productDAO.findAll();
        int resultAfterDelete = resultListAfterDelete.size();

        assertThat(resultAfterSave - resultAfterDelete == 1).isFalse();
    }

    @Test
    void shouldFindById() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());
        productDAO.save(product1);


        Product result = productDAO.findById(product1.getId());

        assertThat(result.equals(product1)).isTrue();
    }

    @Test
    void shouldFindNotByWrongId() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());
        productDAO.save(product1);

        assertThrows(IndexOutOfBoundsException.class, () -> productDAO.findById(UUID.randomUUID()));
    }

    @Test
    void shouldFindByArticle() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());
        productDAO.save(product1);
        String article = "PP-01";

        Product result = productDAO.findByArticle(article);

        assertThat(result.equals(product1)).isTrue();
    }

    @Test
    void shouldNotFindByIdCauseOfWrongArticle() {
        product1 = new Product(
                "prod1",
                2000,
                "PP-01",
                "Super-puper product",
                5
        );
        product1.setId(UUID.randomUUID());
        productDAO.save(product1);
        String article = "00000000";

        assertThrows(IndexOutOfBoundsException.class, () -> productDAO.findByArticle(article));
    }
}