package ua.strongBody.processors.post;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ua.strongBody.models.Product;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductPostProcessorTest {

    private final Product product = new Product();

    private final ProductPostProcessor testInstance = new ProductPostProcessor();

    @ParameterizedTest
    @MethodSource("availabilityTestData")
    void shouldProcessAvailabilityAmount(int amount, int reserved, int expected) {
        product.setAmount(amount);
        product.setReservedAmount(reserved);

        testInstance.postProcess(product);

        assertThat(product.getAvailableAmount()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("sortTestData")
    void shouldSort(List<Product> input, List<Product> expected) {
        testInstance.postProcess(input);

        assertThat(input).isEqualTo(expected);
    }

    private static Stream<Arguments> availabilityTestData() {
        return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(1, 1, 0),
                Arguments.of(10, 5, 5),
                Arguments.of(50, 25, 25));
    }

    private static Stream<Arguments> sortTestData() {
        Product productA = createProductByName("A");
        Product productB = createProductByName("B");
        Product productC = createProductByName("C");

        return Stream.of(
                Arguments.of(Arrays.asList(productA, productB, productC), Arrays.asList(productA, productB, productC)),
                Arguments.of(Arrays.asList(productB, productC, productA), Arrays.asList(productA, productB, productC)),
                Arguments.of(Arrays.asList(productC, productA, productB), Arrays.asList(productA, productB, productC))
        );
    }

    private static Product createProductByName(String name) {
        Product product = new Product();
        product.setName(name);
        return product;
    }
}
