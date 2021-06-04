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
import ua.strongBody.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ComponentScan(basePackages = "ua.strongBody.dao")
class ProductDAOImplIntegrationTest {

    private static final String PRODUCT_NAME1 = "prod1";
    private static final String PRODUCT_NAME2 = "prod2";
    private static final int PRICE1 = 2000;
    private static final int PRICE2 = 3000;
    private static final String ARTICLE1 = "PP-01";
    private static final String ARTICLE2 = "PP-02";
    private static final String DESCRIPTION1 = "Super-puper product1";
    private static final String DESCRIPTION2 = "Wruper-hruper product2";
    private static final int AVAILABLE_AMOUNT1 = 5;
    private static final int AVAILABLE_AMOUNT2 = 7;
    private static final String WRONG_ARTICLE = "00000000";
    private static final String SCRIPT_FILENAME = "schema.sql";

    private Product product1;
    private Product product2;
    private EmbeddedDatabase database;

    @Autowired
    private ProductDAO testInstance;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName(PRODUCT_NAME1);
        product1.setPrice(PRICE1);
        product1.setArticle(ARTICLE1);
        product1.setDescription(DESCRIPTION1);
        product1.setAmount(AVAILABLE_AMOUNT1);

        product2 = new Product();
        product2.setName(PRODUCT_NAME2);
        product2.setPrice(PRICE2);
        product2.setArticle(ARTICLE2);
        product2.setDescription(DESCRIPTION2);
        product2.setAmount(AVAILABLE_AMOUNT2);

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
        product2.setId(UUID.randomUUID());
        testInstance.save(product1);
        testInstance.save(product2);

        List<Product> actual = testInstance.findAll();

        assertThat(actual).contains(product1).contains(product2);
    }

    @Test
    void shouldNotFindProduct() {
        product2.setId(UUID.randomUUID());
        Product unexpected = new Product();
        unexpected.setId(UUID.randomUUID());
        testInstance.save(product1);
        testInstance.save(product2);

        List<Product> actual = testInstance.findAll();

        assertThat(actual.contains(unexpected)).isFalse();
    }

    @Test
    void shouldSave() {
        testInstance.save(product1);

        List<Product> actual = testInstance.findAll();

        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void shouldUpdateById() {
        product2.setId(product1.getId());
        testInstance.save(product1);

        testInstance.updateById(product1.getId(), product2);
        List<Product> actual = testInstance.findAll();

        assertThat(actual).isNotEmpty().contains(product2);
    }

    @Test
    void shouldNotFindProductAfterUpdate() {
        product2.setId(product1.getId());
        testInstance.save(product1);

        testInstance.updateById(product1.getId(), product2);
        List<Product> actual = testInstance.findAll();

        assertThat(actual.contains(product1)).isFalse();
    }

    @Test
    void shouldDeleteById() {
        testInstance.save(product1);

        testInstance.deleteById(product1.getId());
        List<Product> actual = testInstance.findAll();

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldNotDeleteByIdByIrregularId() {
        testInstance.save(product1);

        testInstance.deleteById(UUID.randomUUID());
        List<Product> actual = testInstance.findAll();

        assertThat(actual).contains(product1);
    }

    @Test
    void shouldFindById() {
        testInstance.save(product1);

        Optional<Product> actualProductOptional = testInstance.findById(product1.getId());

        assertThat(actualProductOptional).isPresent();
        Product actualProduct = actualProductOptional.get();
        assertThat(actualProduct).isEqualTo(product1);
    }

    @Test
    void shouldFindNotByWrongId() {
        testInstance.save(product1);

        Optional<Product> actual = testInstance.findById(UUID.randomUUID());

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldFindByArticle() {
        testInstance.save(product1);

        Optional<Product> actualOptionalProduct = testInstance.findByArticle(ARTICLE1);

        assertThat(actualOptionalProduct).isNotEmpty();
        Product actualProduct = actualOptionalProduct.get();
        assertThat(actualProduct).isEqualTo(product1);
    }

    @Test
    void shouldNotFindByIdCauseOfWrongArticle() {
        testInstance.save(product1);

        Optional<Product> actual = testInstance.findByArticle(WRONG_ARTICLE);

        assertThat(actual).isEmpty();
    }
}
